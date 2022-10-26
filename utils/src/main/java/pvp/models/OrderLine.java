package pvp.models;

import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.Product;


public class OrderLine extends PkModel implements pvp.models.interfaces.OrderLine {
    private int unitPrice;
    private int quantity;
    private Integer totalPrice;
    private Product product;
    private Integer productId;

    /**
     * OrderLine()
     * @param pk - the pk of the orderline.
     * @param unitPrice - the unitprice of the orderline.
     * @param quantity - the quantity of the orderline.
     * @param totalPrice - the total price of the orderline.
     * @param product - the product of the orderline.
     */
    public OrderLine(Integer pk, int unitPrice, int quantity, Integer totalPrice, Product product) {
        super(pk);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product = product;
        this.productId = product.getPk();
    }
    /**
     * OrderLine()
     * @param pk - the pk of the orderline.
     * @param unitPrice - the unitprice of the orderline.
     * @param quantity - the quantity of the orderline.
     * @param totalPrice - the total price of the orderline.
     * @param productId - the product ID of the orderline.
     */
    public OrderLine(Integer pk, int unitPrice, int quantity, Integer totalPrice, Integer productId) {
        super(pk);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.productId = productId;
    }

    @Override
    public int getUnitPrice() {
        return this.unitPrice;
    }

    @Override
    public int getTotalPrice() {
        if (this.totalPrice == null) {
            this.calculatePrice();
        }
        return this.totalPrice;
    }

    @Override
    public void calculatePrice() {
        this.totalPrice = this.quantity * this.unitPrice;
    }

    /**
     * setUnitPrice()
     * Sets the unit price to the given value of amount.
     * @param amount
     */
    @Override
    public void setUnitPrice(int amount) {
        this.unitPrice = amount;
        calculatePrice();
    }

    /**
     * setTotalPrice()
     * Set total price to the given value of amount.
     * @param amount - the total price.
     */
    @Override
    public void setTotalPrice(int amount) {
        this.totalPrice = amount;
    }

    @Override
    public Product getProduct() {
        return this.product;
    }

    /**
     * @param product - product to be set.
     */
    @Override
    public void setProduct(Product product) {
        this.product = product;
        this.unitPrice = product.getPrice();
        calculatePrice();
    }

    /**
     * @param id - the ID to be set to the product.
     */
    @Override
    public void setProductId(int id) {
        this.productId = id;
    }

    @Override
    public int getProductId() {
        return this.productId;
    }

    @Override
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * setQuantity()
     * Sets a cuantity, then calculates the price.
     * @param quantity - the quantity to be set.
     */
    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculatePrice();
    }
}