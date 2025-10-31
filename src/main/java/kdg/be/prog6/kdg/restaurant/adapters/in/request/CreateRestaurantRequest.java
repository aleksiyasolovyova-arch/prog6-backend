package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import kdg.be.prog6.kdg.restaurant.adapters.in.response.RestaurantDto;
import kdg.be.prog6.kdg.restaurant.domain.*;
import kdg.be.prog6.kdg.restaurant.ports.in.CreateRestaurantCommand;

import java.util.List;

public record CreateRestaurantRequest(
                                      String name,
                                      RestaurantDto.AddressDto address,
                                      String contactEmail,
                                      List<String> pictureUrls,
                                      int defaultPreparationTimeMinutes,
                                      String cuisineType,
                                      OpeningHoursDto openingHours
) {
    public CreateRestaurantCommand toCommand(OwnerId ownerId) {
        return new CreateRestaurantCommand(
                ownerId,
                name,
                address.toDomain(),
                Email.of(contactEmail),
                pictureUrls,
                CuisineType.valueOf(cuisineType),
                PreparationTime.ofMinutes(defaultPreparationTimeMinutes),
                openingHours.toDomain()
        );
    }

    public record AddressDto(
            String street,
            String postalCode,
            String city
    ) {
        public Address toDomain() {
            return new Address(street, city, postalCode);
        }
    }

    public record OpeningHoursDto(
            String monday,
            String tuesday,
            String wednesday,
            String thursday,
            String friday,
            String saturday,
            String sunday
    ) {
        public OpeningHours toDomain() {
            return OpeningHours.of(
                    monday, tuesday, wednesday,
                    thursday, friday, saturday, sunday
            );
        }
    }
}
