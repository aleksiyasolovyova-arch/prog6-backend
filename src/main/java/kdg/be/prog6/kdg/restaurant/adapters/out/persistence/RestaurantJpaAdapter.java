package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import org.springframework.stereotype.Repository;

@Repository
public class RestaurantJpaAdapter implements RestaurantRepositoryPort {
    private final RestaurantJpaRepository jpaRepo;
    private final RestaurantMapper mapper;


    public RestaurantJpaAdapter(RestaurantJpaRepository jpaRepo, RestaurantMapper mapper) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
    }
    @Override
    public void save(Restaurant restaurant) {
        var entity = mapper.toEntity(restaurant);
        var saved = jpaRepo.save(entity);
        mapper.updateDomain(restaurant, saved);
    }
}
