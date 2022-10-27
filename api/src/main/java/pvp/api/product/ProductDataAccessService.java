package pvp.api.product;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.xml.sax.SAXException;
import pvp.api.xmlparser.ProductParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Repository
public class ProductDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * selectAllProducts()
     * Selects all products from the postgreSQL database.
     */
    List<Product> selectAllProducts(JSONObject json) {
        String sql = "SELECT p.*, (SELECT SUM(ol.quantity) FROM order_line ol INNER JOIN \"order\" o " +
                "ON o.id = ol.order_id AND ol.product_id = p.id";

        if (json.has("age") || json.has("sex") || json.has("customer")) {
            sql = sql + " INNER JOIN \"user\" u ON o.user_id = u.id";
        }

        sql = sql + " WHERE completed = true";

        if (json.has("startTime") && json.has("endTime")){
            String startTime = json.getString("startTime");
            startTime = startTime.replace("-", "");
            String endTime = json.getString("endTime");
            endTime = endTime.replace("-", "");
            sql = sql + " AND o.purchase_date >= '" + startTime + "' " +
                "AND o.purchase_date <= '" + endTime + "'";
        } else if (json.has("startTime")) {
            String startTime = json.getString("startTime");
            startTime = startTime.replace("-", "");
            sql = sql + " AND o.purchase_date >= '" + startTime + "'";
        } else if (json.has("endTime")) {
            String endTime = json.getString("endTime");
            endTime = endTime.replace("-", "");
            sql = sql + " AND o.purchase_date <= '" + endTime + "'";
        }

        if (json.has("age")) {
            LocalDate now = LocalDate.now();
            LocalDate maxAge = now.minusYears(Integer.parseInt(json.getString("age")));
            String maxAgeString = maxAge.toString().replace("-", "");
            LocalDate minAge = maxAge.minusYears(1);
            String minAgeString = minAge.toString().replace("-", "");
            sql = sql + " AND u.compressed_birth_day <= '" + maxAgeString + "' AND u.compressed_birth_day >= '" + minAgeString + "'";
        }

        if (json.has("sex")) {
            sql = sql + " AND u.sex LIKE '" + json.getString("sex") + "'";
        }

        if (json.has("customer")) {
            String customer = json.getString("customer");
            sql = sql + " AND (" +
                    "u.first_name ILIKE '%" + customer + "%' OR " +
                    "u.last_name ILIKE '%" + customer + "%'";
            try {
                Integer customerId = Integer.parseInt(customer);
                sql = sql + " OR u.id ='" + customerId + "'";
            } catch (Exception e) {}
            sql = sql + ")";
        }


        sql = sql + ") sold_count " +
        "FROM product p";

        if (json.has("sku")) {
            String sku = json.getString("sku");
            sql = sql + " WHERE (p.sku ILIKE '%" + sku + "%' OR p.name ILIKE '%" + sku + "%')";
        }
        System.out.println(sql);
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (json.length() == 0 ){
            return products;
        }

        return products.stream().filter(prod -> {
            pvp.models.interfaces.Product product = (pvp.models.interfaces.Product) prod;
            if (product.getSoldCount() > 0) {
                return true;
            }
            return false;
        }).toList();

    }

    /**
     * getProductBySku()
     * fetches product by sku from the postgreSQL database.
     * @param sku - sku of the product being fetched.
     */
    Product getProductBySku(String sku) {
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p " +
                "WHERE sku = '" + sku + "'";
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    Product getProductByOldId(Integer id){
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p " +
                "WHERE old_id = '" + id + "'";
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    /**
     * getProductById()
     * fetches product by ID from the postgreSQL database.
     * @param id - the id of the product being fetched.
     */
    public Product getProductById(int id) {
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p " +
                "WHERE id = '" + id + "'";
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    /**
     * selectAllProductsBySearch()
     * selects all products by search from the postgreSQL database.
     * @param search
     */
    public List<Product> selectAllProductsBySearch(String search) {
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p " +
                "WHERE name ILIKE '%" + search + "%' OR " +
                "sku ILIKE '%" + search + "%'";

        return jdbcTemplate.query(sql, mapProductsFomDb());
    }

    /**
     * insertProducts()
     * inserts product to the postgreSQL database.
     * @param oldId  - id from the product catalog or null.
     * @param product - the product to be inserted.
     */
    void insertProduct(pvp.models.interfaces.Product product, Integer oldId) {
        Product dbProduct = null;
        try {
            if (oldId != null) {
                dbProduct = this.getProductByOldId(oldId);
            } else {
                dbProduct = this.getProductById(product.getPk());
            }
        } catch (NullPointerException e){
            System.out.println("Error");
        }

        if (dbProduct == null) {
            if (oldId != null) {
                String sql = "" +
                        "INSERT INTO product (" +
                        " sku, " +
                        " price," +
                        " name," +
                        " vat," +
                        " keywords," +
                        " old_id) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                jdbcTemplate.update(
                        sql,
                        product.getSku(),
                        product.getPrice(),
                        product.getName(),
                        product.getVat(),
                        product.getKeywordsString(),
                        oldId
                );
            } else {
                String sql = "" +
                        "INSERT INTO product (" +
                        " sku, " +
                        " price," +
                        " name," +
                        " vat," +
                        " keywords) " +
                        "VALUES (?, ?, ?, ?, ?)";
                jdbcTemplate.update(
                        sql,
                        product.getSku(),
                        product.getPrice(),
                        product.getName(),
                        product.getVat(),
                        product.getKeywordsString()
                    );
            }
        } else {
            String sql = "" +
                    "UPDATE product" +
                    " SET sku = '" + product.getSku() + "', " +
                    " price = '" + product.getPrice() + "'," +
                    " name = '" + product.getName() + "', " +
                    " vat = '" + product.getVat() + "', " +
                    " keywords = '" + product.getKeywordsString() + "' " +
                    " WHERE id = " + product.getPk();
            jdbcTemplate.update(sql);
        }
    }

    /**
     * mapOrderLinesFromDb()
     * Returns an arrow-function that can be run to generate product from a database row.
     */
    private RowMapper<Product> mapProductsFomDb() {
        return (resultSet, i) -> {
            String sku = resultSet.getString("sku");
            int pk = resultSet.getInt("id");
            int price = resultSet.getInt("price");
            String name = resultSet.getString("name");
            Integer soldCount = resultSet.getInt("sold_count");
            String keywords = resultSet.getString("keywords");
            if (keywords == null) {
                keywords = "";
            }

            Product product = new pvp.api.product.Product(
                    pk,
                    sku,
                    soldCount,
                    price,
                    name,
                    resultSet.getInt("vat"),
                    ""
            );
            product.setKeywords(keywords);
            return product;
        };
    }
}
