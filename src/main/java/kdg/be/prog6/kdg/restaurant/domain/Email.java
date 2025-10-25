package kdg.be.prog6.kdg.restaurant.domain;

public record Email(String value) {
    public Email {
        if (!value.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    public static Email of(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        return new Email(email);
    }
}
