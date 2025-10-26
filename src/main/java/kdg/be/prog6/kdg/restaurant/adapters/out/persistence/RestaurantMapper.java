package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    
    @Autowired(required = false)
    private DishDraftMapper dishDraftMapper;

    public RestaurantEntity toEntity(Restaurant restaurantDomain) {
        var e = new RestaurantEntity();
        e.setId(restaurantDomain.getId().uuid());
        e.setOwnerId(restaurantDomain.getOwnerId().uuid());
        e.setName(restaurantDomain.getName());
        e.setAddress(AddressEmbeddable.from(restaurantDomain.getAddress()));
        e.setContactEmail(restaurantDomain.getContactEmail().value());
        e.setPictureUrls(restaurantDomain.getPictureUrls());
        e.setCuisineType(restaurantDomain.getCuisineType().name());
        e.setDefaultPreparationTimeMinutes(restaurantDomain.getDefaultPreparationTime().toMinutes());
        e.setOpeningHours(OpeningHoursEmbeddable.from(restaurantDomain.getOpeningHours()));
        e.setCreatedAt(restaurantDomain.getCreatedAt());

        // Map the published dishes
        e.setPublishedDishes(
            restaurantDomain.getPublishedDishes().stream()
                .map(dish -> {
                    // Convert Dish domain to DishEntity
                    DishEntity entity = new DishEntity();
                    entity.setId(dish.getId().uuid());
                    entity.setRestaurantId(restaurantDomain.getId().uuid());
                    entity.setDetails(DishDetailsEmbeddable.from(dish.getDetails()));
                    entity.setAvailableForOrder(dish.isAvailableForOrder());
                    entity.setCreatedAt(dish.getCreatedAt());
                    entity.setUpdatedAt(dish.getUpdatedAt());
                    return entity;
                })
                .toList()
        );
        
        if (dishDraftMapper != null) {
            e.setDraftDishes(
                restaurantDomain.getDraftDishes().stream()
                    .map(dishDraftMapper::toEntity)
                    .toList()
            );
        }

        return e;
    }

    public void updateDomain(Restaurant domain, RestaurantEntity entity) {
        // This method can be used if you need to update the domain object after persistence
    }
    
    public Restaurant toDomain(RestaurantEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Restaurant restaurant = Restaurant.reconstitute(
                RestaurantId.from(entity.getId()),
                entity.getOwnerId() != null 
                    ? OwnerId.from(entity.getOwnerId().toString())
                    : null,
                entity.getName(),
                entity.getAddress().toDomain(),
                Email.of(entity.getContactEmail()),
                entity.getPictureUrls(),
                CuisineType.valueOf(entity.getCuisineType()),
                PreparationTime.ofMinutes(entity.getDefaultPreparationTimeMinutes()),
                entity.getOpeningHours().toDomain(),
                entity.getCreatedAt()
        );

        // Reconstitute draft dishes if mapper is available
        if (dishDraftMapper != null && entity.getDraftDishes() != null) {
            entity.getDraftDishes().forEach(draftEntity -> {
                DishDraft draft = dishDraftMapper.toDomain(draftEntity);
                restaurant.addReconstitutedDraft(draft);
            });
        }

        //Reconstitute published dishes
        if (entity.getPublishedDishes() != null) {
            entity.getPublishedDishes().forEach(dishEntity -> {
                // Convert DishEntity back to Dish domain object
                Dish dish = Dish.reconstitute(
                        DishId.from(dishEntity.getId()),
                        RestaurantId.from(dishEntity.getRestaurantId()),
                        dishEntity.getDetails().toDomain(),
                        dishEntity.isAvailableForOrder(),
                        dishEntity.getCreatedAt(),
                        dishEntity.getUpdatedAt()
                );
                restaurant.addReconstitutedDish(dish);
            });
        }

        return restaurant;
    }
}
