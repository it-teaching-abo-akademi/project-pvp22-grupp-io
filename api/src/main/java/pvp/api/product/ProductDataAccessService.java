package pvp.api.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pvp.api.user.User;

import java.util.List;
import java.util.UUID;

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

    int insertProduct(String sku, Product product) {
        String sql = "" +
                "INSERT INTO product (" +
                " sku, " +
                " price," +
                " name) " +
                "VALUES (?, ?, ?)";
        return jdbcTemplate.update(
                sql,
                sku,
                product.getPrice(),
                product.getName()
        );
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
