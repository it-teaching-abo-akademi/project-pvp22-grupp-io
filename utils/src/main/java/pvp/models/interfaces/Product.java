package pvp.models.interfaces;

import java.util.Set;

public interface Product extends PkModel, JSONSerializable {
    public void setName(String name);
    public void setPrice(int price);
    public void setSku(String sku);
    public void setSoldCount(Integer sold);
    public void setVat(Integer vat);
    public void setKeywords(Set<String> keywords);
    public void setKeywords(String keywords);


    public String getName();
    public int getPrice();
    public String getSku();
    public Integer getSoldCount();
    public Integer getVat();
    public String getKeywordsString();
    public Set<String> getKeywords();

}
