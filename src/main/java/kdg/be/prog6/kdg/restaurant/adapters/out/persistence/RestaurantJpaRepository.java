package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, UUID> {
    Optional<RestaurantEntity> findByOwnerId(UUID uuid);
}
