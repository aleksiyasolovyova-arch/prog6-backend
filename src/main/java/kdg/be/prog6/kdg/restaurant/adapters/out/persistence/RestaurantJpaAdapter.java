package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.OwnerId;
import kdg.be.prog6.kdg.restaurant.domain.Restaurant;
import kdg.be.prog6.kdg.restaurant.domain.RestaurantId;
import kdg.be.prog6.kdg.common.RestaurantNotFoundException;
import kdg.be.prog6.kdg.restaurant.ports.out.RestaurantRepositoryPort;
import org.jmolecules.event.types.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RestaurantJpaAdapter implements RestaurantRepositoryPort {
    private final RestaurantJpaRepository jpaRepo;
    private final RestaurantMapper mapper;
    private final ApplicationEventPublisher eventPublisher;


    public RestaurantJpaAdapter(RestaurantJpaRepository jpaRepo, RestaurantMapper mapper, ApplicationEventPublisher eventPublisher) {
        this.jpaRepo = jpaRepo;
        this.mapper = mapper;
        this.eventPublisher = eventPublisher;
    }
    @Override
    public void save(Restaurant restaurant) {
        var entity = mapper.toEntity(restaurant);
        var saved = jpaRepo.save(entity);
        mapper.updateDomain(restaurant, saved);

        publishDomainEvents(restaurant);
    }

    @Override
    public Optional<Restaurant> findById(RestaurantId id) {
        var entity = jpaRepo.findById(id.uuid());
        if (entity.isEmpty()) {
            throw new RestaurantNotFoundException(
                "Restaurant with ID " + id.uuid() + " not found"
            );
        }
        return Optional.ofNullable(mapper.toDomain(entity.get()));
    }

    @Override
    public Optional<Restaurant> findByOwnerId(OwnerId ownerId) {
        // Fetch the restaurant entity using the JPA repository
        return jpaRepo
                .findByOwnerId(ownerId.uuid()) // JPA call using UUID as the key
                .map(mapper::toDomain); // Convert to domain entity using the mapper
    }

    @Override
    public List<Restaurant> findAll() {
        return jpaRepo.findAll().stream()
                .map(mapper::toDomain)
                .toList();
    }

    private void publishDomainEvents(Restaurant restaurant) {
        List<DomainEvent> events = restaurant.getDomainEvents();

        for (DomainEvent event : events) {
            eventPublisher.publishEvent(event);
        }

        restaurant.clearDomainEvents();
    }
}
