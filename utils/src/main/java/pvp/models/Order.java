package pvp.models;

import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.OrderLine;
import pvp.models.interfaces.Payment;
import pvp.models.interfaces.Product;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class Order extends PkModel implements pvp.models.interfaces.Order {
    private int totalPrice;
    private Set<pvp.models.interfaces.OrderLine> orderLines;
    private Set<Payment> payments;
    private pvp.models.interfaces.User user;
    private int userId;
    public Order(
            Integer pk, int totalPrice,
            Set<pvp.models.interfaces.OrderLine> orderLines,
            pvp.models.interfaces.User user,
            Set<Payment> payments
    ) {
        super(pk);
        this.totalPrice = totalPrice;
        this.payments = payments;
        this.orderLines = orderLines;
        this.user = user;
    }
    public Order(
            int pk, int totalPrice,
            Set<pvp.models.interfaces.OrderLine> orderLines,
            int userId,
            Set<Payment> payments
    ) {
        super(pk);
        this.totalPrice = totalPrice;
        this.payments = payments;
        this.orderLines = orderLines;
        this.userId = userId;
    }

    @Override
    public void updateTotalPrice() {
        this.totalPrice = this.orderLines.stream().mapToInt(orderLine -> {
            return orderLine.getTotalPrice();
        }).sum();
    }

    public int getTotalPrice(){
        return this.totalPrice;
    }

    @Override
    public int getTotalPaidAmount() {
        return this.payments.stream().mapToInt(payment -> {
            return payment.getAmount();
        }).sum();
    }

    public Set<pvp.models.interfaces.OrderLine> getOrderLines() {
        return this.orderLines;
    }

    @Override
    public void addOrderLine(pvp.models.interfaces.OrderLine orderLine) { this.orderLines.add(orderLine); }

    @Override
    public void addProduct(Product product) {
        Optional<OrderLine> possibleOrderLine = findOrderLineByProduct(product);
        if (possibleOrderLine.isPresent()) {
            OrderLine orderLine = possibleOrderLine.get();
            int q = orderLine.getQuantity() + 1;
            orderLine.setQuantity(q);
        } else {
            int quantity = 1;
            OrderLine orderLine = new pvp.models.OrderLine(null, product.getPrice(), quantity, product.getPrice() * quantity, product);
            this.addOrderLine(orderLine);
        }
        updateTotalPrice();
    }

    private Optional<OrderLine> findOrderLineByProduct(Product product) {
        Stream<OrderLine> unFilteredLines = this.orderLines.stream();
        Stream<OrderLine> filteredLines = unFilteredLines.filter(orderLine -> {
            if (orderLine.getProductId() == product.getPk()) {
                return true;
            }
            return false;
        });
        return filteredLines.findFirst();
    }

    @Override
    public Optional<OrderLine> getOrderLineById(int id) {
        Stream<OrderLine> unFilteredLines = this.orderLines.stream();
        Stream<OrderLine> filteredLines = unFilteredLines.filter(orderLine -> {
            if (orderLine.getPk() == id) { return true; }
            return false;
        });
        return filteredLines.findFirst();
    }

    @Override
    public void removeOrderLine(pvp.models.interfaces.OrderLine orderLine) {
        this.orderLines.remove(orderLine);
    }

    @Override
    public void removeOrderLine(int orderLineId) {
        Optional<OrderLine> line = this.getOrderLineById(orderLineId);
        if (line.isEmpty()) {
            return;
        }
        this.orderLines.remove(line);
    }

    @Override
    public void setUser(pvp.models.interfaces.User user) {
        this.user = user;
    }

    @Override
    public void setUserId(int id) {
        this.userId = id;
    }

    public pvp.models.interfaces.User getUser() {
        return this.user;
    }

    @Override
    public int getUserId() {
        if (this.user != null) {
            return this.user.getPk();
        }
        return this.userId;
    }

    @Override
    public void createPayment(int amount, PaymentType paymentType) {
            this.payments.add(new pvp.models.Payment(null, amount, paymentType, this));
    }

    @Override
    public void createPayment(int amount, String paymentType) {
        this.payments.add(new pvp.models.Payment(null, amount, paymentType, this));
    }
}