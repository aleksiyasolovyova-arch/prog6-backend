package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, String> {
}
