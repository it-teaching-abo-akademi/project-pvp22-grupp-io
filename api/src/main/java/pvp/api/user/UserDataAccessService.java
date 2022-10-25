package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class UserDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<User> selectAllUsers() {
        String sql = "" +
                "SELECT " +
                " id, " +
                " customer_reference, " +
                " name " +
                "FROM \"user\"";

        return jdbcTemplate.query(sql, mapUsersFomDb());
    }

    User getUserByReference(UUID reference) {
        String sql = "" +
                "SELECT " +
                " id, " +
                " customer_reference, " +
                " name " +
                "FROM \"user\" " +
                "WHERE customer_reference = '" + reference + "'";
        List<User> users = jdbcTemplate.query(sql, mapUsersFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }
    public User getUserById(int id) {
        String sql = "" +
                "SELECT " +
                " id, " +
                " customer_reference, " +
                " name " +
                "FROM \"user\" " +
                "WHERE id = '" + id + "'";
        List<User> users = jdbcTemplate.query(sql, mapUsersFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    int insertUser(User user) {
        pvp.models.interfaces.User dbUser = this.getUserByReference(user.getCustomerReference());

        if (dbUser == null) {
            String sql = "" +
                    "INSERT INTO \"user\" (" +
                    " customer_reference, " +
                    " name) " +
                    "VALUES (?, ?)";
            return jdbcTemplate.update(
                    sql,
                    user.getCustomerReference(),
                    user.getName()
            );
        } else {
            String sql = "UPDATE \"user\"" +
                    " SET customer_reference = '" + user.getCustomerReference() + "', " +
                    "name = '" + user.getName() + "' " +
                    "WHERE id = " + user.getPk();
            return jdbcTemplate.update(sql);
        }
    }
    private RowMapper<User> mapUsersFomDb() {
        return (resultSet, i) -> {
            String userIdStr = resultSet.getString("customer_reference");
            UUID userId = UUID.fromString(userIdStr);

            int pk = resultSet.getInt("id");

            String name = resultSet.getString("name");
            return new User(
                    pk,
                    userId,
                    name
            );
        };
    }
}
