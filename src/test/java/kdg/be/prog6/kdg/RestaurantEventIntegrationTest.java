package kdg.be.prog6.kdg;

import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.*;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RestaurantEventIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(RestaurantEventIntegrationTest.class);

    @Autowired
    private RestaurantRepositoryPort restaurantRepository;

    private OpeningHours createTestOpeningHours() {
        TimeRange businessHours = new TimeRange(LocalTime.of(11, 0), LocalTime.of(22, 0));
        List<TimeRange> openingTimes = List.of(businessHours);

        Map<DayOfWeek, List<TimeRange>> hoursPerDay = Map.of(
                DayOfWeek.MONDAY, openingTimes,
                DayOfWeek.TUESDAY, openingTimes,
                DayOfWeek.WEDNESDAY, openingTimes,
                DayOfWeek.THURSDAY, openingTimes,
                DayOfWeek.FRIDAY, List.of(new TimeRange(LocalTime.of(11, 0), LocalTime.of(23, 0))),
                DayOfWeek.SATURDAY, List.of(new TimeRange(LocalTime.of(11, 0), LocalTime.of(23, 0))),
                DayOfWeek.SUNDAY, List.of(new TimeRange(LocalTime.of(12, 0), LocalTime.of(22, 0)))
        );

        return new OpeningHours(hoursPerDay);
    }

    @Test
    void shouldCompleteFullEventLifecycle() {
        log.info("=== Starting full event lifecycle test ===");

        // Given: A restaurant
        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Complete Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );

        // Event 1: RestaurantCreatedEvent
        restaurantRepository.save(restaurant);
        log.info("✓ Restaurant created: {}", restaurant.getId());
        assertThat(restaurant.getId()).isNotNull();
        restaurant.clearDomainEvents();

        // Event 2: DishDraftCreatedEvent
        DishDetails details = new DishDetails(
                "Margherita",
                DishType.MAIN,
                Set.of(FoodTag.KETO),
                "Fresh mozzarell<",
                Money.of(BigDecimal.valueOf(12.99), "EUR"),
                "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu"
        );
        DishDraft draft = restaurant.createDraftForNewDish(details);
        restaurantRepository.save(restaurant);
        log.info("✓ Draft created");
        assertThat(restaurant.getDraftDishes()).isNotEmpty();
        restaurant.clearDomainEvents();

        // Event 3: DishPublishedEvent
        DishId dishId = restaurant.publishDraft(draft.getId());
        restaurantRepository.save(restaurant);
        log.info("✓ Dish published: {}", dishId);
        assertThat(restaurant.getPublishedDishes()).isNotEmpty();
        assertThat(restaurant.getDraftDishes()).isEmpty();
        restaurant.clearDomainEvents();

        // Event 4: RestaurantOpenedEvent
        restaurant.openManually();
        restaurantRepository.save(restaurant);
        log.info("✓ Restaurant opened manually");
        assertThat(restaurant.isAcceptingOrders()).isTrue();
        restaurant.clearDomainEvents();

        // Event 5: DishMarkedOutOfStockEvent
        restaurant.markDishOutOfStock(dishId);
        restaurantRepository.save(restaurant);
        log.info("✓ Dish marked out of stock");
        assertThat(restaurant.getAvailableDishes()).isEmpty();
        restaurant.clearDomainEvents();

        // Event 6: DishMarkedInStockEvent
        restaurant.markDishInStock(dishId);
        restaurantRepository.save(restaurant);
        log.info("✓ Dish marked in stock");
        assertThat(restaurant.getAvailableDishes()).isNotEmpty();
        restaurant.clearDomainEvents();

        // Event 7: RestaurantClosedEvent
        restaurant.closeManually();
        restaurantRepository.save(restaurant);
        log.info("✓ Restaurant closed manually");
        assertThat(restaurant.isAcceptingOrders()).isFalse();

        log.info("=== All events processed successfully ===");
    }

    @Test
    void shouldHandleMultipleDishes() {
        log.info("=== Starting multiple dishes test ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Multi-Dish Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        // Add multiple dishes
        List<String> dishNames = List.of("Carbonara", "Margherita", "Lasagna", "Risotto");
        for (String dishName : dishNames) {
            DishDetails details = new DishDetails(dishName,
                    DishType.MAIN,
                    Set.of(FoodTag.KETO),
                    "Fresh mozzarell<",
                    Money.of(BigDecimal.valueOf(12.99), "EUR"),
                    "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu");
            DishDraft draft = restaurant.createDraftForNewDish(details);
            restaurant.publishDraft(draft.getId());
            log.info("✓ Published: {}", dishName);
        }
        restaurantRepository.save(restaurant);

        assertThat(restaurant.getPublishedDishes()).hasSize(4);
        log.info("=== Multiple dishes test completed ===");
    }

    @Test
    void shouldHandleDraftDiscard() {
        log.info("=== Starting draft discard test ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Discard Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        // Create and discard draft
        DishDetails details = new DishDetails("Margherita",
                DishType.MAIN,
                Set.of(FoodTag.KETO),
                "Fresh mozzarell<",
                Money.of(BigDecimal.valueOf(12.99), "EUR"),
                "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu");
        DishDraft draft = restaurant.createDraftForNewDish(details);
        assertThat(restaurant.getDraftDishes()).hasSize(1);

        restaurant.discardDraft(draft.getId());
        restaurantRepository.save(restaurant);

        assertThat(restaurant.getDraftDishes()).isEmpty();
        log.info("✓ Draft discarded successfully");
    }
}
