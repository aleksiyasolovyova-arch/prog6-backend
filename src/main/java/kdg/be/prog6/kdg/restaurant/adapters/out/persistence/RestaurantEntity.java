package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    private String id;
    private String ownerId;
    private String name;
    @Embedded
    private AddressEmbeddable address;
    private String contactEmail;
    @ElementCollection
    private List<String> pictureUrls;
    private String cuisineType;
    private int defaultPreparationTimeMinutes;
    @Embedded
    private OpeningHoursEmbeddable openingHours;
    private LocalDateTime createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AddressEmbeddable getAddress() {
        return address;
    }

    public void setAddress(AddressEmbeddable address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public List<String> getPictureUrls() {
        return pictureUrls;
    }

    public void setPictureUrls(List<String> pictureUrls) {
        this.pictureUrls = pictureUrls;
    }

    public String getCuisineType() {
        return cuisineType;
    }

    public void setCuisineType(String cuisineType) {
        this.cuisineType = cuisineType;
    }

    public int getDefaultPreparationTimeMinutes() {
        return defaultPreparationTimeMinutes;
    }

    public void setDefaultPreparationTimeMinutes(int defaultPreparationTimeMinutes) {
        this.defaultPreparationTimeMinutes = defaultPreparationTimeMinutes;
    }

    public OpeningHoursEmbeddable getOpeningHours() {
        return openingHours;
    }

    public void setOpeningHours(OpeningHoursEmbeddable openingHours) {
        this.openingHours = openingHours;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
