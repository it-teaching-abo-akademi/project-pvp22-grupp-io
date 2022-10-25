package pvp.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ProductDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<Product> selectAllProducts() {
        String sql = "" +
                "SELECT * " +
                "FROM product";

        return jdbcTemplate.query(sql, mapProductsFomDb());
    }

    Product getProductBySku(String sku) {
        String sql = "" +
                "SELECT *" +
                "FROM product " +
                "WHERE sku = '" + sku + "'";
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    public Product getProductById(int id) {
        String sql = "" +
                "SELECT *" +
                "FROM product " +
                "WHERE id = '" + id + "'";
        List<Product> products = jdbcTemplate.query(sql, mapProductsFomDb());
        if (products.size() > 0) {
            return products.get(0);
        }
        return null;
    }

    public List<Product> selectAllProductsBySearch(String search) {
        String sql = "" +
                "SELECT * " +
                "FROM product " +
                "WHERE name ILIKE '%" + search + "%' OR " +
                "sku ILIKE '%" + search + "%'";

        return jdbcTemplate.query(sql, mapProductsFomDb());
    }

    void insertProduct(String sku, Product product) {
        Product dbProduct = this.getProductById(product.getPk());

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
    private RowMapper<Product> mapProductsFomDb() {
        return (resultSet, i) -> {
            String sku = resultSet.getString("sku");
            int pk = resultSet.getInt("id");
            int price = resultSet.getInt("price");
            String name = resultSet.getString("name");

            return new Product(
                    pk,
                    sku,
                    price,
                    name
            );
        };
    }
}
