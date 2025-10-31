package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.*;

import java.util.List;
import java.util.Optional;

public interface RestaurantManagementPort {
    Optional<Restaurant> getRestaurantByOwner(OwnerId ownerId);

    Restaurant createRestaurant(
            OwnerId ownerId,
            String name,
            Address address,
            Email email,
            List<String> pictureUrls,
            CuisineType cuisineType,
            PreparationTime defaultPreparationTime,
            OpeningHours openingHours
    );

    Restaurant updateSettings(
            OwnerId ownerId,
            String name,
            OpeningHours openingHours,
            List<String> pictureUrls
    );

    Restaurant updateContact(OwnerId ownerId, Email email, Address address);

    Restaurant overrideOpeningStatus(OwnerId ownerId, boolean forceOpen);
}
