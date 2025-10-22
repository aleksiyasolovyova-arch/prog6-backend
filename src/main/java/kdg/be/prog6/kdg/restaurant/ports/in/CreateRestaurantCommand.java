package kdg.be.prog6.kdg.restaurant.ports.in;

import kdg.be.prog6.kdg.restaurant.domain.*;

import java.util.List;

public record CreateRestaurantCommand(
        OwnerId ownerId,
        String name,
        Address address,
        Email email,
        List<String> pictureUrls,
        CuisineType cuisineType,
        PreparationTime defaultPreparationTime,
        OpeningHours openingHours
) {}
