package pvp.models;

import pvp.models.abstractModels.PkModel;

public class Product extends PkModel implements pvp.models.interfaces.Product {
    private int price;
    private String sku;
    private String name;
    private Integer soldCount;

    /**
     * Product()
     * @param pk - pk of the product.
     * @param price - price of the product.
     * @param name - name of the product.
     * @param sku - sku of the product.
     * @param soldCount - amount sold of the product.
     */
    public Product(Integer pk, int price, String name, String sku, Integer soldCount) {
        super(pk);
        this.price = price;
        this.name = name;
        this.sku = sku;
        this.soldCount = soldCount;
    }

    /**
     * @param name
     */
    @Override
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param price
     */
    @Override
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * @param sku
     */
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

    @Override
    public Integer getSoldCount() {
        return this.soldCount;
    }

    /**
     * @param sold
     */
    @Override
    public void setSoldCount(Integer sold) {
        this.soldCount = sold;
    }
}
