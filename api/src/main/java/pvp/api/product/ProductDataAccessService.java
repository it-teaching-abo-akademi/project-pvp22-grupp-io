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
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p";

        return jdbcTemplate.query(sql, mapProductsFomDb());
    }

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

    public List<Product> selectAllProductsBySearch(String search) {
        String sql = "" +
                "SELECT p.*, (select count(*) from order_line r where r.product_id = p.id) sold_count " +
                "FROM product p " +
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
