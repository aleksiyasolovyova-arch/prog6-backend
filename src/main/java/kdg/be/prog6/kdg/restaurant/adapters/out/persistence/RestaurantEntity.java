package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurants")
public class RestaurantEntity {
    @Id
    @Column(columnDefinition = "uuid")
    private UUID id;  // ← Change from String to UUID

    @Column(name = "owner_id", columnDefinition = "uuid")
    private UUID ownerId;
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

    // Add the relationship to dish drafts
    @OneToMany(mappedBy = "restaurantId", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DishDraftEntity> draftDishes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")  // Foreign key in dishes table
    private List<DishEntity> publishedDishes = new ArrayList<>();


    public RestaurantEntity() {
    }

    public RestaurantEntity(UUID id, UUID ownerId, String name, AddressEmbeddable address, String contactEmail, List<String> pictureUrls, String cuisineType, int defaultPreparationTimeMinutes, OpeningHoursEmbeddable openingHours, LocalDateTime createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.name = name;
        this.address = address;
        this.contactEmail = contactEmail;
        this.pictureUrls = pictureUrls;
        this.cuisineType = cuisineType;
        this.defaultPreparationTimeMinutes = defaultPreparationTimeMinutes;
        this.openingHours = openingHours;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
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

    public List<DishDraftEntity> getDraftDishes() {
        return draftDishes;
    }

    public void setDraftDishes(List<DishDraftEntity> draftDishes) {
        this.draftDishes = draftDishes;
    }

    public List<DishEntity> getPublishedDishes() {
        return publishedDishes;
    }

    public void setPublishedDishes(List<DishEntity> publishedDishes) {
        this.publishedDishes = publishedDishes;
    }
}
