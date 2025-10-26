package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.restaurant.domain.exceptions.RestaurantNotFoundException;
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

    @Override
    public Restaurant findById(RestaurantId id) {
        var entity = jpaRepo.findById(id.uuid());
        if (entity.isEmpty()) {
            throw new RestaurantNotFoundException(
                "Restaurant with ID " + id.uuid() + " not found"
            );
        }
        return mapper.toDomain(entity.get());
    }
}
