package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import kdg.be.prog6.kdg.restaurant.domain.*;
import org.springframework.stereotype.Component;

@Component
public class RestaurantMapper {
    public RestaurantEntity toEntity(Restaurant restaurantDomain) {
        var e = new RestaurantEntity();
        e.setId(restaurantDomain.getId().uuid());
        e.setOwnerId(restaurantDomain.getOwnerId().uuid());
        e.setName(restaurantDomain.getName());
        e.setAddress(AddressEmbeddable.from(restaurantDomain.getAddress()));
        e.setContactEmail(restaurantDomain.getContactEmail().value());
        e.setPictureUrls(restaurantDomain.getPictureUrls());
        e.setCuisineType(restaurantDomain.getCuisineType().name());
        e.setDefaultPreparationTimeMinutes(restaurantDomain.getDefaultPreparationTime().toMinutes());
        e.setOpeningHours(OpeningHoursEmbeddable.from(restaurantDomain.getOpeningHours()));
        e.setCreatedAt(restaurantDomain.getCreatedAt());
        return e;
    }

    public void updateDomain(Restaurant domain, RestaurantEntity entity) {

    }
    
    public Restaurant toDomain(RestaurantEntity entity) {
        return Restaurant.reconstitute(
                RestaurantId.from(entity.getId()),
                OwnerId.from(entity.getOwnerId().toString()),
                entity.getName(),
                entity.getAddress().toDomain(),
                Email.of(entity.getContactEmail()),
                entity.getPictureUrls(),
                CuisineType.valueOf(entity.getCuisineType()),
                PreparationTime.ofMinutes(entity.getDefaultPreparationTimeMinutes()),
                entity.getOpeningHours().toDomain(),
                entity.getCreatedAt()
        );
    }
}
