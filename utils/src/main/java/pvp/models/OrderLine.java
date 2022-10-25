package pvp.models;

import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.Product;


public class OrderLine extends PkModel implements pvp.models.interfaces.OrderLine {
    private int unitPrice;
    private int quantity;
    private Integer totalPrice;
    private Product product;
    private Integer productId;

    public OrderLine(Integer pk, int unitPrice, int quantity, Integer totalPrice, Product product) {
        super(pk);
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.product = product;
        this.productId = product.getPk();
    }
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

    @Override
    public void setUnitPrice(int amount) {
        this.unitPrice = amount;
    }

    @Override
    public void setTotalPrice(int amount) {
        this.totalPrice = amount;
    }

    @Override
    public Product getProduct() {
        return this.product;
    }

    @Override
    public void setProduct(Product product) {
        this.product = product;
    }

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

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculatePrice();
    }
}