package pvp.models.interfaces;

public interface Product extends PkModel {
    public void setName(String name);
    public void setPrice(int price);
    public void setSku(String sku);
    public String getName();
    public int getPrice();
    public String getSku();

    public Integer getSoldCount();
    public void setSoldCount(Integer sold);
}
