package kdg.be.prog6.kdg.restaurant.domain;

import kdg.be.prog6.kdg.common.DishNotFoundException;
import kdg.be.prog6.kdg.common.events.*;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.*;
import org.jmolecules.event.types.DomainEvent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class Restaurant {
    private static final int MAX_AVAILABLE_DISHES = 10;
    private RestaurantId restaurantId;
    private OwnerId ownerId;
    private String name;
    private Address address;
    private Email email;
    private List<String> pictureUrls;
    private CuisineType cuisineType;
    private PreparationTime defaultPreparationTime;
    private OpeningHours openingHours;

    //Opening state
    private boolean manuallyOpen = false;
    private boolean manuallyClosed = false;

    private LocalDateTime createdAt;

    private List<DomainEvent> events = new ArrayList<>();
    private List<Dish> publishedDishes = new ArrayList<>();
    private List<DishDraft> draftDishes = new ArrayList<>();


    private Restaurant() {
    }

    public static Restaurant create(
            OwnerId ownerId,
            String name,
            Address address,
            Email email,
            List<String> pictureUrls,
            CuisineType cuisineType,
            PreparationTime defaultPreparationTime,
            OpeningHours openingHours
    ) {
        Restaurant restaurant = new Restaurant();
        restaurant.restaurantId = RestaurantId.generate();
        restaurant.ownerId = ownerId;
        restaurant.name = name;
        restaurant.address = address;
        restaurant.email = email;
        restaurant.pictureUrls = new ArrayList<>(pictureUrls);
        restaurant.cuisineType = cuisineType;
        restaurant.defaultPreparationTime = defaultPreparationTime;
        restaurant.openingHours = openingHours;
        restaurant.createdAt = LocalDateTime.now();

        restaurant.registerEvent(new RestaurantCreatedEvent(
                restaurant.restaurantId.uuid(),
                ownerId.uuid(),
                name,
                cuisineType.name()
        ));

        return restaurant;
    }

    public static Restaurant reconstitute(
            RestaurantId id,
            OwnerId ownerId,
            String name,
            Address address,
            Email contactEmail,
            List<String> pictureUrls,
            CuisineType cuisineType,
            PreparationTime defaultPreparationTime,
            OpeningHours openingHours,
            LocalDateTime createdAt) {

        Restaurant restaurant = new Restaurant();
        restaurant.restaurantId = id;
        restaurant.ownerId = ownerId;
        restaurant.name = name;
        restaurant.address = address;
        restaurant.email = contactEmail;
        restaurant.pictureUrls = new ArrayList<>(pictureUrls);
        restaurant.cuisineType = cuisineType;
        restaurant.defaultPreparationTime = defaultPreparationTime;
        restaurant.openingHours = openingHours;
        restaurant.createdAt = createdAt;

        return restaurant;
    }

    public DishDraft createDraftForNewDish(DishDetails details) {
        DishDraft draft = DishDraft.createNew(
                DraftId.generate(),
                this.restaurantId,
                details
        );
        draftDishes.add(draft);

        registerEvent(new DishDraftCreatedEvent(draft.getId().uuid(), this.restaurantId.uuid(), true));
        return draft;
    }

    public DishDraft createDraftForExistingDish(DishId dishId) {
        Dish publishedDish = findPublishedDishById(dishId);

        if (findDraftForDish(dishId).isPresent()) {
            throw new DraftAlreadyExistsException(
                    "A draft already exists for dish: " + dishId
            );
        }

        DishDraft draft = DishDraft.createFromPublished(
                DraftId.generate(),
                publishedDish
        );
        draftDishes.add(draft);

        registerEvent(new DishEditStartedEvent(draft.getId().uuid(), dishId.uuid(), this.restaurantId.uuid()));
        return draft;
    }

    public void editDraft(DraftId draftId, DishDetails details) {
        DishDraft draft = findDraftById(draftId);
        draft.editDetails(details);
    }

    public void discardDraft(DraftId draftId) {
        DishDraft draft = findDraftById(draftId);
        draftDishes.remove(draft);

        registerEvent(new DishDraftDiscardedEvent(draftId.uuid(), this.restaurantId.uuid()));
    }

    // domain/Restaurant.java
    public DishId publishDraft(DraftId draftId) {
        DishDraft draft = findDraftById(draftId);

        if (draft.isNewDish() && countAvailableDishes() >= MAX_AVAILABLE_DISHES) {
            throw new DishLimitExceededException("Dish limit has been exceeded.");
        }

        DishId publishedDishId;

        if (draft.isNewDish()) {
            Dish newDish = draft.toPublishedDish();
            publishedDishes.add(newDish);
            publishedDishId = newDish.getId();
            registerEvent(new DishPublishedEvent(newDish.getId().uuid(), this.restaurantId.uuid(), newDish.getName()));
        } else {
            Dish existingDish = findPublishedDishById(draft.getOriginalDishId());
            draft.applyChangesTo(existingDish);
            publishedDishId = existingDish.getId();
            registerEvent(new DishUpdatedEvent(existingDish.getId().uuid(), this.restaurantId.uuid(), existingDish.getName()));
        }

        draftDishes.remove(draft);
        return publishedDishId;
    }


    public void publishAllDrafts() {
        if (draftDishes.isEmpty()) {
            return;
        }

        long newDishesCount = draftDishes.stream()
                .filter(DishDraft::isNewDish)
                .count();

        if (countAvailableDishes() + newDishesCount > MAX_AVAILABLE_DISHES) {
            throw new DishLimitExceededException(
                    String.format("Cannot publish all drafts. Would exceed %d-dish limit. " +
                                    "Currently %d available, attempting to add %d new dishes.",
                            MAX_AVAILABLE_DISHES, countAvailableDishes(), newDishesCount)
            );
        }

        List<DishDraft> draftsToPublish = new ArrayList<>(draftDishes);
        draftsToPublish.forEach(draft -> publishDraft(draft.getId()));

        registerEvent(new AllDraftsPublishedEvent(this.restaurantId.uuid(), draftsToPublish.size()));
    }

    public void schedulePublishAllDrafts(LocalDateTime publishAt) {
        if (publishAt.isBefore(LocalDateTime.now())) {
            throw new InvalidScheduleException("Cannot schedule publication in the past");
        }

        if (draftDishes.isEmpty()) {
            throw new NoDraftsToPublishException("No drafts available to schedule");
        }

        long newDishesCount = draftDishes.stream()
                .filter(DishDraft::isNewDish)
                .count();

        if (countAvailableDishes() + newDishesCount > MAX_AVAILABLE_DISHES) {
            throw new DishLimitExceededException(
                    "Cannot schedule drafts. Would exceed 10-dish limit when published.");
        }

        draftDishes.forEach(draft -> draft.schedulePublish(publishAt));

        registerEvent(new DraftsScheduledEvent(
                this.restaurantId.uuid(),
                publishAt,
                draftDishes.size()
        ));
    }

//    public void publishScheduledDrafts(LocalDateTime currentTime) {
//        List<DishDraft> readyToPublish = draftDishes.stream()
//                .filter(draft -> draft.isScheduledFor(currentTime))
//                .toList();
//
//        if (readyToPublish.isEmpty()) {
//            return;
//        }
//
//        long newDishesCount = readyToPublish.stream()
//                .filter(DishDraft::isNewDish)
//                .count();
//
//        if (countAvailableDishes() + newDishesCount > MAX_AVAILABLE_DISHES) {
//            registerEvent(new ScheduledPublishFailedEvent(
//                    this.restaurantId.uuid(),
//                    "Would exceed 10-dish limit",
//                    readyToPublish.size()
//            ));
//            throw new DishLimitExceededException(
//                    "Scheduled publish cancelled: would exceed 10-dish limit"
//            );
//        }
//
//        readyToPublish.forEach(draft -> publishDraft(draft.getId()));
//    }

    public void unpublishDish(DishId dishId) {
        Dish dish = findPublishedDishById(dishId);
        publishedDishes.remove(dish);

        registerEvent(new DishUnpublishedEvent(dishId.uuid(), this.restaurantId.uuid(), dish.getName()));
    }

    //Both marking out and in stock are immediate, so no drafts involved
    public void markDishOutOfStock(DishId dishId) {
        Dish dish = findPublishedDishById(dishId);
        dish.markAsOutOfStock();

        registerEvent(new DishMarkedOutOfStockEvent(dishId.uuid(), this.restaurantId.uuid()));
    }

    public void markDishInStock(DishId dishId) {
        Dish dish = findPublishedDishById(dishId);
        dish.markInStock();

        registerEvent(new DishMarkedInStockEvent(dishId.uuid(), this.restaurantId.uuid()));
    }


    public boolean isAcceptingOrders() {
        if (manuallyClosed) {
            return false;
        }
        if (manuallyOpen) {
            return true;
        }
        return openingHours.isOpenNow();
    }

    public void openManually() {
        if (manuallyOpen) {
            return;
        }
        this.manuallyOpen = true;
        this.manuallyClosed = false;

        registerEvent(new RestaurantOpenedEvent(this.restaurantId.uuid(), true));
    }

    public void closeManually() {
        if (manuallyClosed) {
            return;
        }
        this.manuallyOpen = false;
        this.manuallyClosed = true;

        registerEvent(new RestaurantClosedEvent(this.restaurantId.uuid(), true));
    }

    public void resetManualOverride() {
        boolean wasManuallyControlled = manuallyOpen || manuallyClosed;
        this.manuallyOpen = false;
        this.manuallyClosed = false;

        if (wasManuallyControlled) {
            if (openingHours.isOpenNow()) {
                registerEvent(new RestaurantOpenedEvent(this.restaurantId.uuid(), false));
            } else {
                registerEvent(new RestaurantClosedEvent(this.restaurantId.uuid(), false));
            }
        }
    }

    //Calculation for busyness based on pending orders. This is used for estimating preparation times
    public double calculateBusyiness(int pendingOrders) {
        if (pendingOrders == 0) {
            return 1.0;
        }
        return 1.0 + (pendingOrders * 0.1);
    }

    private Dish findPublishedDishById(DishId dishId) {
        return publishedDishes.stream()
                .filter(d -> d.getId().equals(dishId))
                .findFirst()
                .orElseThrow(() -> new DishNotFoundException("Published dish not found: " + dishId));
    }

    private DishDraft findDraftById(DraftId draftId) {
        return draftDishes.stream()
                .filter(d -> d.getId().equals(draftId))
                .findFirst()
                .orElseThrow(() -> new DraftNotFoundException("Draft not found: " + draftId));
    }

    private Optional<DishDraft> findDraftForDish(DishId dishId) {
        return draftDishes.stream()
                .filter(draft -> !draft.isNewDish() && draft.getOriginalDishId().equals(dishId))
                .findFirst();
    }

    private long countAvailableDishes() {
        return publishedDishes.stream()
                .filter(Dish::isAvailableForOrder)
                .count();
    }

    private void registerEvent(DomainEvent event) {
        events.add(event);
    }

    public int getPendingChangesCount() {
        return draftDishes.size();
    }

    public List<Dish> getPublishedDishes() {
        return new ArrayList<>(publishedDishes);
    }

    public List<Dish> getAvailableDishes() {
        return publishedDishes.stream()
                .filter(Dish::isAvailableForOrder)
                .collect(Collectors.toList());
    }

    public List<DishDraft> getDraftDishes() {
        return new ArrayList<>(draftDishes);
    }

    public boolean hasDraftForDish(DishId dishId) {
        return findDraftForDish(dishId).isPresent();
    }

    public List<DomainEvent> getDomainEvents() {
        return new ArrayList<>(events);
    }

    public void clearDomainEvents() {
        events.clear();
    }

    // Used by repository for reconstitution
    public void addReconstitutedDish(Dish dish) {
        publishedDishes.add(dish);
    }

    public void addReconstitutedDraft(DishDraft draft) {
        draftDishes.add(draft);
    }


    public RestaurantId getId() {
        return restaurantId;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public Email getContactEmail() {
        return email;
    }

    public List<String> getPictureUrls() {
        return new ArrayList<>(pictureUrls);
    }

    public CuisineType getCuisineType() {
        return cuisineType;
    }

    public PreparationTime getDefaultPreparationTime() {
        return defaultPreparationTime;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public boolean isManuallyOpen() {
        return manuallyOpen;
    }

    public boolean isManuallyClosed() {
        return manuallyClosed;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}