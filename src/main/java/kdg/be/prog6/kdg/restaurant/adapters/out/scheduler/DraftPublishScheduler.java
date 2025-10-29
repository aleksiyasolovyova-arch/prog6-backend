package kdg.be.prog6.kdg.restaurant.adapters.out.scheduler;

import kdg.be.prog6.kdg.restaurant.adapters.out.persistence.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.DishDraft;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class DraftPublishScheduler {
    private static final Logger log = LoggerFactory.getLogger(DraftPublishScheduler.class);

    private final RestaurantRepositoryPort restaurantRepository;

    public DraftPublishScheduler(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Runs every minute to check for scheduled draft publications
     */
    @Scheduled(cron = "0 * * * * *")
    @Transactional
    public void publishScheduledDrafts() {
        log.debug("Checking for scheduled draft publications...");

        try {
            // Get all restaurants
            List<Restaurant> restaurants = restaurantRepository.findAll();

            int publishedCount = 0;

            for (Restaurant restaurant : restaurants) {
                List<DishDraft> draftDishes = restaurant.getDraftDishes();

                // Find drafts ready to publish
                List<DishDraft> readyDrafts = draftDishes.stream()
                        .filter(DishDraft::shouldPublishNow)
                        .toList();

                if (!readyDrafts.isEmpty()) {
                    log.info("Publishing {} scheduled drafts for restaurant {}",
                            readyDrafts.size(), restaurant.getId());

                    for (DishDraft draft : readyDrafts) {
                        restaurant.publishDraft(draft.getId());
                        publishedCount++;
                    }

                    restaurantRepository.save(restaurant);
                }
            }

            if (publishedCount > 0) {
                log.info("Published {} scheduled drafts", publishedCount);
            }

        } catch (Exception e) {
            log.error("Error in draft publish scheduler", e);
        }
    }
}
