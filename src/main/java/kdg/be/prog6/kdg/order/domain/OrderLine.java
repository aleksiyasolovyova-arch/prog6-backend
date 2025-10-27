package kdg.be.prog6.kdg.order.domain;

import java.math.BigDecimal;
import java.util.UUID;

public class OrderLine {
    private UUID dishId;
    private String dishName;
    private BigDecimal priceAtOrderTime;
    private int quantity;

    public OrderLine() {
    }

    public static OrderLine create(UUID dishId, String dishName, BigDecimal price, int quantity) {
        OrderLine line = new OrderLine();
        line.dishId = dishId;
        line.dishName = dishName;
        line.priceAtOrderTime = price;
        line.quantity = quantity;
        return line;
    }

    public BigDecimal getTotalPrice() {
        return priceAtOrderTime.multiply(BigDecimal.valueOf(quantity));
    }


    public UUID getDishId() { return dishId; }
    public String getDishName() { return dishName; }
    public BigDecimal getPrice() { return priceAtOrderTime; }
    public int getQuantity() { return quantity; }
}
