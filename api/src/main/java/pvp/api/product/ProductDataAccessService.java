package pvp.api.product;

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
    List<Product> selectAllProducts() {
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p";

        return jdbcTemplate.query(sql, mapProductsFomDb());
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
     * @param sku  - the sku of the product to be inserted.
     * @param product - the product to be inserted.
     */
    void insertProduct(String sku, pvp.models.interfaces.Product product) {
        Product dbProduct = null;
        try {
            dbProduct = this.getProductById(product.getPk());
        } catch (NullPointerException e){
            System.out.println("Error");
        }

        if (dbProduct == null) {
            String sql = "" +
                    "INSERT INTO product (" +
                    " sku, " +
                    " price," +
                    " name) " +
                    "VALUES (?, ?, ?)";
            jdbcTemplate.update(
                    sql,
                    sku,
                    product.getPrice(),
                    product.getName()
            );
        } else {
            String sql = "" +
                    "UPDATE product" +
                    " SET sku = '" + sku + "', " +
                    " price = '" + product.getPrice() + "'," +
                    " name = '" + product.getName() + "' " +
                    " WHERE id = " + product.getPk();
            System.out.println(sql);
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

            return new Product(
                    pk,
                    sku,
                    soldCount,
                    price,
                    name
            );
        };
    }
}
