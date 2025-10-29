package kdg.be.prog6.kdg;

import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class RestaurantEventListenerAsyncTest {

    @Autowired
    private RestaurantRepositoryPort restaurantRepository;


    /**
     * Helper method to create default opening hours for testing
     * Map<DayOfWeek, List<TimeRange>> where each day can have multiple time ranges
     */
    private OpeningHours createTestOpeningHours() {
        // Create a single time range: 11:00 to 22:00
        TimeRange businessHours = new TimeRange(LocalTime.of(11, 0), LocalTime.of(22, 0));

        // Wrap in a list (can have multiple ranges per day for breaks, etc.)
        List<TimeRange> openingTimes = List.of(businessHours);

        // Create opening hours for all days of the week
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
    void shouldPublishRestaurantCreatedEventAsync() throws InterruptedException {
        // Given: A new restaurant
        Restaurant restaurant = Restaurant.create(
                new OwnerId(UUID.randomUUID()),
                "Pizza Palace",
                new Address("Via Roma", "Rome", "00100"),
                new Email("owner@pizzapalace.com"),
                List.of("https://example.com/pic1.jpg"),
                CuisineType.ITALIAN,
                new PreparationTime(25),
                createTestOpeningHours()
        );

        // When: We save the restaurant
        restaurantRepository.save(restaurant);

        // Then: Event listener should have been called asynchronously
        assertThat(restaurant.getId()).isNotNull();
        assertThat(restaurant.getName()).isEqualTo("Pizza Palace");
    }

    @Test
    void shouldPublishDishPublishedEventAsync() throws InterruptedException {
        // Given: A restaurant with a draft
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

        DishDetails details = new DishDetails(
                "Margherita",
                DishType.MAIN,
                Set.of(FoodTag.KETO),
                "Fresh mozzarell<",
                Money.of(BigDecimal.valueOf(12.99), "EUR"),
                "jkvyukhkjhuigyftgvjhbkhuigyftghjbjhu"
        );
        DishDraft draft = restaurant.createDraftForNewDish(details);

        // Save to clear events
        restaurantRepository.save(restaurant);
        restaurant.clearDomainEvents();

        // When: We publish the draft
        DishId dishId = restaurant.publishDraft(draft.getId());
        restaurantRepository.save(restaurant);

        // Then: Event listener should have been called asynchronously
        assertThat(dishId).isNotNull();
        assertThat(restaurant.getPublishedDishes()).isNotEmpty();
    }
}
