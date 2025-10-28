package kdg.be.prog6.kdg.order.domain;

public record CustomerInfo(
        String name,
        DeliveryAddress address,
        String email
) {
    public CustomerInfo {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Customer name is required");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Valid email is required");
        }
        if (address == null) {
            throw new IllegalArgumentException("Delivery address is required");
        }
    }
}
