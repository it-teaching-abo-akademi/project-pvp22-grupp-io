package pvp.models;

import pvp.models.abstractModels.PkModel;

public class OrderLine extends PkModel {
    private int price;
    private Product product;
    private int quantity;

    public OrderLine(String pk, Product product, int price, int quantity) {
        super(pk);
        this.price = price;
        this.product = product;
        this.quantity = quantity;
    }

    public int getPrice(){
        return this.price;
    }

    public Product getProduct() {
        return this.product;
    }

    public int getQuantity() {
        return this.quantity;
    }
}