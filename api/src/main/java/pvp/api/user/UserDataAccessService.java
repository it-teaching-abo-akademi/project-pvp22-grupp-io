package pvp.api.user;

import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pvp.models.PaymentType;
import pvp.models.Sex;
import pvp.models.interfaces.BonusCard;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Repository
public class UserDataAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * selectAllUsers()
     * selects all users from the postgreSQL database.
     */
    List<User> selectAllUsers() {
        String sql = "" +
                "SELECT *" +
                "FROM \"user\"";

        return jdbcTemplate.query(sql, mapUsersFomDb());
    }

    /**
     * getUserByReference()
     * fetches user from the postgreSQL database.
     */
    User getUserBySearch(String search) {
        String sql = "" +
                "SELECT *" +
                "FROM \"user\" u " +
                "INNER JOIN bonus_card bc ON bc.user_id = u.id " +
                "WHERE u.first_name ILIKE '%" + search + "%' OR " +
                "u.last_name ILIKE '%" + search + "%' OR " +
                "bc.holderName ILIKE '%" + search + "%'";
        List<User> users = jdbcTemplate.query(sql, mapUsersFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    User getUserByCard(String number, Integer month, Integer year) {
        String sql = "" +
                "SELECT * " +
                "FROM \"user\" u " +
                "INNER JOIN bonus_card bc ON bc.user_id = u.id " +
                "WHERE bc.number = '" + number + "' AND " +
                "bc.good_thru_month = '" + month + "' AND " +
                "bc.good_thru_year = '" + year + "'";
        System.out.println(sql);
        List<User> users = jdbcTemplate.query(sql, mapUsersFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    /**
     * getUserById()
     * fetches user by ID from the postgreSQL database.
     */
    public User getUserById(int id) {
        String sql = "" +
                "SELECT * " +
                "FROM \"user\" " +
                "WHERE id = '" + id + "'";
        List<User> users = jdbcTemplate.query(sql, mapUsersFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    /**
     * insertUser()
     * inserts user into the postgreSQL databasen.
     */
    int insertUser(User user) {
        pvp.models.interfaces.User dbUser = this.getUserById(user.getPk());

        ZonedDateTime birth = user.getBirthDay();
        String StringBirthDay = "";
        LocalDate birthDate = null;
        if (birth != null) {
            birthDate = birth.toLocalDate();
            StringBirthDay = birth.toString();
        }

        if (dbUser == null) {
            String sql = "" +
                    "INSERT INTO \"user\" (" +
                    "first_name, " +
                    "last_name, " +
                    "birth_day, " +
                    "sex, " +
                    "bonus_points, " +
                    "compressed_birth_day) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(
                    sql,
                    user.getFirstName(),
                    user.getLastName(),
                    StringBirthDay,
                    user.getSex().name(),
                    user.getBonusPoints(),
                    birthDate
            );
        } else {
            String sql = "UPDATE \"user\"" +
                    " SET " +
                    "first_name = '" + user.getFirstName() + "', " +
                    "last_name = '" + user.getLastName() + "'," +
                    "birth_day = '" + StringBirthDay + "', " +
                    "sex = '" + user.getSex().name() + "', " +
                    "bonus_points = '" + user.getBonusPoints() + "', " +
                    "compressed_birth_day = '" + birthDate + "' " +
                    "WHERE id = " + user.getPk();
            return jdbcTemplate.update(sql);
        }
    }

    /**
     * mapUsersFomDb()
     * Returns a arrow-function that can be run to generate user from a database row.
     */
    private RowMapper<User> mapUsersFomDb() {
        return (resultSet, i) -> {
            Sex sex = Sex.valueOf(resultSet.getString("sex"));
            String birthdayString = resultSet.getString("birth_day");
            ZonedDateTime birthday = null;
            if (!birthdayString.equals("")) {
                birthday = ZonedDateTime.parse(birthdayString);
            }

            return new User(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    sex,
                    birthday,
                    new HashSet<BonusCard>(),
                    resultSet.getInt("bonus_points")
            );
        };
    }
}
