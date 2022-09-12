package pvp.models;

import pvp.models.abstractModels.PkModel;

public class Product extends PkModel {
    private int price;
    private String sku;
    private String name;

    public Product(String pk, String sku, int price, String name) {
        super(pk);
        this.price = price;
        this.sku = sku;
        this.name = name;
    }

    public int getPrice(){
        return this.price;
    }

    public String getSku() {
        return this.sku;
    }
    public String getName() { return this.name; }
}
