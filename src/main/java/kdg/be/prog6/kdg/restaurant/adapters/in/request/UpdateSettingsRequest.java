package kdg.be.prog6.kdg.restaurant.adapters.in.request;

import kdg.be.prog6.kdg.restaurant.domain.OpeningHours;

import java.util.List;

public record UpdateSettingsRequest(
        String name,
        OpeningHours openingHours,
        List<String> pictureUrls
) {
}
