package kdg.be.prog6.kdg;

import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
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
class OrderRestaurantEventListenerTest {

    private static final Logger log = LoggerFactory.getLogger(OrderRestaurantEventListenerTest.class);

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
    void shouldReactToRestaurantOpenedEvent() {
        log.info("=== Testing Order context reaction to RestaurantOpenedEvent ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        // When: Restaurant is opened manually
        restaurant.openManually();
        restaurantRepository.save(restaurant);

        // Then: Restaurant should be accepting orders
        assertThat(restaurant.isAcceptingOrders()).isTrue();
        log.info("✓ Order context should now accept orders for this restaurant");
    }

    @Test
    void shouldReactToRestaurantClosedEvent() {
        log.info("=== Testing Order context reaction to RestaurantClosedEvent ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurant.openManually();
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        // When: Restaurant is closed manually
        restaurant.closeManually();
        restaurantRepository.save(restaurant);

        // Then: Restaurant should not be accepting orders
        assertThat(restaurant.isAcceptingOrders()).isFalse();
        log.info("✓ Order context should stop accepting orders for this restaurant");
    }

    @Test
    void shouldReactToDishPublishedEvent() {
        log.info("=== Testing Order context reaction to DishPublishedEvent ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        DishDetails details = new DishDetails( "Margherita",
                DishType.MAIN,
                Set.of(FoodTag.KETO),
                "Fresh mozzarell<",
                Money.of(BigDecimal.valueOf(12.99), "EUR"),
                "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu");
        DishDraft draft = restaurant.createDraftForNewDish(details);
        restaurant.publishDraft(draft.getId());
        restaurantRepository.save(restaurant);

        assertThat(restaurant.getPublishedDishes()).isNotEmpty();
        log.info("✓ Order context menu should be updated with published dish");
    }

    @Test
    void shouldReactToDishUnpublishedEvent() {
        log.info("=== Testing Order context reaction to DishUnpublishedEvent ===");

        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Test Restaurant",
                new Address("Main St", "TestCity", "12345"),
                new Email("test@restaurant.com"),
                List.of(),
                CuisineType.ITALIAN,
                new PreparationTime(30),
                createTestOpeningHours()
        );
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        DishDetails details = new DishDetails( "Margherita",
                DishType.MAIN,
                Set.of(FoodTag.KETO),
                "Fresh mozzarell<",
                Money.of(BigDecimal.valueOf(12.99), "EUR"),
                "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu");
        DishDraft draft = restaurant.createDraftForNewDish(details);
        DishId dishId = restaurant.publishDraft(draft.getId());
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        restaurant.unpublishDish(dishId);
        restaurantRepository.save(restaurant);

        assertThat(restaurant.getPublishedDishes()).isEmpty();
        log.info("✓ Order context menu should be updated to remove unpublished dish");
    }
}
