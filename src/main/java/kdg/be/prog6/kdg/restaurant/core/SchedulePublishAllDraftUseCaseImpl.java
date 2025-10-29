package kdg.be.prog6.kdg.restaurant.core;

import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.DishLimitExceededException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.InvalidScheduleException;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.NoDraftsToPublishException;
import kdg.be.prog6.kdg.restaurant.ports.in.SchedulePublishAllDraftsCommand;
import kdg.be.prog6.kdg.restaurant.ports.in.SchedulePublishAllDraftsPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SchedulePublishAllDraftUseCaseImpl implements SchedulePublishAllDraftsPort {
    private static final Logger log = LoggerFactory.getLogger(SchedulePublishAllDraftUseCaseImpl.class);

    private final RestaurantRepositoryPort restaurantRepository;

    public SchedulePublishAllDraftUseCaseImpl(RestaurantRepositoryPort restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    @Transactional
    public void schedulePublishAllDrafts(SchedulePublishAllDraftsCommand command) {
        log.info("Scheduling publish all drafts for restaurant {} at {}",
                command.restaurantId(),
                command.publishAt());

        try {
            Restaurant restaurant = restaurantRepository.findById(new RestaurantId(command.restaurantId()))
                    .orElseThrow(() -> {
                        log.error("Restaurant not found: {}", command.restaurantId());
                        return new RestaurantNotFoundException("Restaurant not found: " + command.restaurantId());
                    });

            if (command.publishAt().isBefore(LocalDateTime.now())) {
                log.warn("Attempted to schedule publication in the past: {}", command.publishAt());
                throw new InvalidScheduleException("Cannot schedule publication in the past");
            }

            if (restaurant.getDraftDishes().isEmpty()) {
                log.warn("No drafts to schedule for restaurant: {}", command.restaurantId());
                throw new NoDraftsToPublishException("No drafts available to schedule");
            }

            restaurant.schedulePublishAllDrafts(command.publishAt());

            restaurantRepository.save(restaurant);

            log.info("Successfully scheduled {} drafts for restaurant {} to publish at {}",
                    restaurant.getDraftDishes().size(),
                    command.restaurantId(),
                    command.publishAt());

        } catch (InvalidScheduleException | NoDraftsToPublishException | DishLimitExceededException e) {
            log.error("Failed to schedule drafts: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error scheduling drafts", e);
            throw new RuntimeException("Failed to schedule drafts", e);
        }
    }
}
