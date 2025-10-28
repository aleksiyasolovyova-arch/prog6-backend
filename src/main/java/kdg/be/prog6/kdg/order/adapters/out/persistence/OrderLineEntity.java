package kdg.be.prog6.kdg.order.adapters.out.persistence;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_lines")
public class OrderLineEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "dish_id", nullable = false, columnDefinition = "uuid")
    private UUID dishId;

    @Column(name = "dish_name", nullable = false)
    private String dishName;

    @Column(name = "price_at_order_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal priceAtOrderTime;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getDishId() {
        return dishId;
    }

    public void setDishId(UUID dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public BigDecimal getPriceAtOrderTime() {
        return priceAtOrderTime;
    }

    public void setPriceAtOrderTime(BigDecimal priceAtOrderTime) {
        this.priceAtOrderTime = priceAtOrderTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
