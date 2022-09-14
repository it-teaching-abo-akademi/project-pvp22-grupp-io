package pvp.models.interfaces;

public interface OrderLine extends PkModel {
    public int getUnitPrice();
    public int getTotalPrice();
    public void calculatePrice();
    public void setUnitPrice(int amount);
    public void setTotalPrice(int amount);

    public Product getProduct();
    public void setProduct(Product product);
    public void setProductId(int id);
    public int getProductId();

    public int getQuantity();
    public void setQuantity(int quantity);
}
