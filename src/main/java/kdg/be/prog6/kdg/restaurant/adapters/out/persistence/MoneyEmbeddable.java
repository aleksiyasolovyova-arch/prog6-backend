package kdg.be.prog6.kdg.restaurant.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kdg.be.prog6.kdg.restaurant.domain.Money;

import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MoneyEmbeddable {
    @Column(name = "price_amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "price_currency", length = 3, nullable = false)
    private String currency;

    // JPA requires no-arg constructor
    protected MoneyEmbeddable() {}

    private MoneyEmbeddable(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    // Static factory: Domain → Persistence
    public static MoneyEmbeddable from(Money money) {
        if (money == null) {
            return null;
        }
        return new MoneyEmbeddable(money.amount(), money.currency().toString());
    }

    // Convert back: Persistence → Domain
    public Money toDomain() {
        return Money.of(amount, currency);
    }

    // Getters for JPA
    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoneyEmbeddable)) return false;
        MoneyEmbeddable that = (MoneyEmbeddable) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(currency, that.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return amount + " " + currency;
    }
}
