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

    private boolean complete;

    /**
     * Order()
     * Instantiates order, given there is a user.
     * @param pk
     * @param totalPrice
     * @param orderLines
     * @param user
     * @param payments
     * @param complete
     */
    public Order(
            Integer pk, int totalPrice,
            Set<pvp.models.interfaces.OrderLine> orderLines,
            pvp.models.interfaces.User user,
            Set<Payment> payments,
            boolean complete
    ) {
        super(pk);
        this.totalPrice = totalPrice;
        this.payments = payments;
        this.orderLines = orderLines;
        this.user = user;
        this.complete = complete;
    }

    /**
     * Order()
     * Instantiates order, given there is an user ID.
     * @param pk
     * @param totalPrice
     * @param orderLines
     * @param userId
     * @param payments
     * @param complete
     */
    public Order(
            int pk, int totalPrice,
            Set<pvp.models.interfaces.OrderLine> orderLines,
            int userId,
            Set<Payment> payments,
            boolean complete
    ) {
        super(pk);
        this.totalPrice = totalPrice;
        this.payments = payments;
        this.orderLines = orderLines;
        this.userId = userId;
        this.complete = complete;
    }

    /**
     * updateTotalPrice()
     * Updates the total price of the orderline.
     */
    @Override
    public void updateTotalPrice() {
        this.totalPrice = this.orderLines.stream().mapToInt(orderLine -> {
            return orderLine.getTotalPrice();
        }).sum();
    }

    /**
     * getTotalPrice()
     * Fetches total price.
     */
    public int getTotalPrice(){
        return this.totalPrice;
    }

    /**
     * isComplete()
     * Returns true if order is complete, and false if order is uncomplete.
     */
    @Override
    public boolean isComplete() { return this.complete; }

    /**
     * getTotalPaidAmount()
     * Fetches total paid amount.
     */
    @Override
    public int getTotalPaidAmount() {
        return this.payments.stream().mapToInt(payment -> {
            return payment.getAmount();
        }).sum();
    }

    /**
     * getPayment()
     * fetches payment.
     */
    public Set<Payment> getPayments() {
        return this.payments;
    }

    /**
     * getOrderLines()
     * Fetches orderlines as a Set of orderlines.
     */
    public Set<pvp.models.interfaces.OrderLine> getOrderLines() {
        return this.orderLines;
    }

    /**
     * addOrderLine()
     * @param orderLine - orderline to be added.
     */
    @Override
    public void addOrderLine(pvp.models.interfaces.OrderLine orderLine) { this.orderLines.add(orderLine); }

    /**
     * addProduct()
     * If orderline with product exists, increase amount.
     * If orderline with product does not exist, create new orderline.
     * @param product - the product to be added and compared to other products with.
     */
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

    /**
     * findOrderLineByProduct()
     * If the given product's pk equals orderline's product's id, return true.
     * If the given product's pk does not equal orderline's product's id, return false.
     * @param product - product used to find order.
     */
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

    /**
     * getOrderLineById()
     * If orderline's pk equals the id given, return true.
     * If orderline's pk does not equal the id given, return false.
     * @param id - id of the orderline to be fetched.
     */
    @Override
    public Optional<OrderLine> getOrderLineById(int id) {
        Stream<OrderLine> unFilteredLines = this.orderLines.stream();
        Stream<OrderLine> filteredLines = unFilteredLines.filter(orderLine -> {

            if (orderLine.getPk() != null && orderLine.getPk() == id) { return true; }
            return false;
        });
        return filteredLines.findFirst();
    }

    /**
     * removerOrderLine()
     * @param orderLine - orderline to be removed.
     */
    @Override
    public void removeOrderLine(pvp.models.interfaces.OrderLine orderLine) {
        this.orderLines.remove(orderLine);
    }

    /**
     * Checks if the orderline ID exists.
     * If it is empty, it returns nothing.
     * Otherwise, it removew the orderline.
     * @param orderLineId - id of orderline to check.
     */
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
    public void setIsComplete(boolean isComplete) { this.complete = isComplete; }

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

    /**
     * createPayment()
     * Creates payment with the properties: pk, amount, paymentType and order.
     * @param amount - a property of the payment.
     * @param paymentType - a property of the payment.
     */
    @Override
    public void createPayment(int amount, PaymentType paymentType) {
            this.payments.add(new pvp.models.Payment(null, amount, paymentType, this));
    }

    /**
     * createPayment()
     * Creates payment with the properties: pk, amount, paymentTupe and order.
     * @param amount - a property of the payment.
     * @param paymentType - a property of the payment
     */
    @Override
    public void createPayment(int amount, String paymentType) {
        this.payments.add(new pvp.models.Payment(null, amount, paymentType, this));
    }

    /**
     * createPayment()
     * Creates payment with the properties: pk, amount, paymentTupe and order.
     * @param amount - a property of the payment.
     * @param paymentType - a property of the payment.
     * @param pk - a property of the payment.
     */
    @Override
    public void createPayment(Integer pk, int amount, String paymentType) {
        this.payments.add(new pvp.models.Payment(pk, amount, paymentType, this));
    }

    @Override
    public void setPk(int pk) {
        this.pk = pk;
    }
}