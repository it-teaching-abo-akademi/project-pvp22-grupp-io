package pvp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.Payment;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

public class Product extends PkModel implements pvp.models.interfaces.Product {
    private int price;
    private String sku;
    private String name;
    private Integer soldCount;
    private Integer vat;
    private Set<String> keywords;

    /**
     * Product()
     * @param pk - pk of the product.
     * @param price - price of the product.
     * @param name - name of the product.
     * @param sku - sku of the product.
     * @param soldCount - amount sold of the product.
     */
    public Product(
            Integer pk, int price, String name,
            String sku, Integer soldCount,
            Integer vat, Set<String> keywords
    ) {
        super(pk);
        this.price = price;
        this.name = name;
        this.sku = sku;
        this.soldCount = soldCount;
        this.vat = vat;
        this.keywords = keywords;
    }

    public Product(
            Integer pk, int price, String name,
            String sku, Integer soldCount,
            Integer vat, String keywords
    ) {
        super(pk);
        this.price = price;
        this.name = name;
        this.sku = sku;
        this.soldCount = soldCount;
        this.vat = vat;
        setKeywords(keywords);
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

    @Override
    public Integer getVat() {
        return this.vat;
    }

    @Override
    public String getKeywordsString() {
        return String.join(",", this.keywords.stream().toList());
    }

    @Override
    public Set<String> getKeywords() {
        return this.keywords;
    }

    /**
     * @param sold
     */
    @Override
    public void setSoldCount(Integer sold) {
        this.soldCount = sold;
    }

    @Override
    public void setVat(Integer vat) {
        this.vat = vat;
    }

    @Override
    public void setKeywords(Set<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public void setKeywords(String keywords) {
        Set<String> keywordsSet = new HashSet<String>();
        for (String s : keywords.split(",")) {
            keywordsSet.add(s);
        }
        this.keywords = keywordsSet;
    }

    @Override
    public JSONObject getJsonOfObject() {
        JSONObject json = new JSONObject();
        json.put("pk", this.getPk());
        json.put("price", this.getPrice());
        json.put("keywords", this.getKeywordsString());
        json.put("name", this.getName());
        json.put("soldCount", this.getSoldCount());
        json.put("sku", this.getSku());
        json.put("vat", this.getVat());
        return json;
    }

    public static pvp.models.interfaces.Product getObjectFromJson(JSONObject json) {
        pvp.models.interfaces.Product product = new Product(
                json.getInt("pk"),
                json.getInt("price"),
                json.getString("name"),
                json.getString("sku"),
                json.getInt("soldCount"),
                json.getInt("vat"),
                new HashSet<String>()
        );
        try {
            String keywords = json.getString("keywords");
            product.setKeywords(keywords);
        } catch (JSONException e) {
            JSONArray keywords = json.getJSONArray("keywords");
            Set<String> keywordSet = new HashSet<String>();
            keywords.forEach(keyword -> keywordSet.add((String) keyword));
            product.setKeywords(keywordSet);
        }
        return product;
    }
}
