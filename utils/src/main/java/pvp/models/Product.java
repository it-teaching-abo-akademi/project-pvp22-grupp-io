package pvp.models;

import pvp.models.abstractModels.PkModel;

public class Product extends PkModel implements pvp.models.interfaces.Product {
    private int price;
    private String sku;
    private String name;

    public Product(Integer pk, int price, String name, String sku) {
        super(pk);
        this.price = price;
        this.name = name;
        this.sku = sku;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getPrice() {
        return this.price;
    }

    @Override
    public String getSku() {
        return this.sku;
    }
}
