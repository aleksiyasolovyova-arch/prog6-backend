package kdg.be.prog6.kdg.restaurant.adapters.in.response;

import kdg.be.prog6.kdg.restaurant.domain.Address;
import kdg.be.prog6.kdg.restaurant.domain.CuisineType;
import kdg.be.prog6.kdg.restaurant.domain.OpeningHours;

import java.util.UUID;

record RestaurantResponseDto(
        UUID id,
        String name,
        Address address,
        CuisineType cuisineType,
        OpeningHours openingHours,
        boolean acceptingOrders,
        UUID ownerId
) {}
