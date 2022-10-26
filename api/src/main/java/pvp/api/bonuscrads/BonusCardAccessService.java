package pvp.api.bonuscrads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import pvp.models.interfaces.BonusCard;

import java.util.List;
import java.util.UUID;

@Repository
public class BonusCardAccessService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BonusCardAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    List<BonusCard> getAllBonusCards() {
        String sql = "" +
                "SELECT * " +
                "FROM bonus_card";

        return jdbcTemplate.query(sql, mapBonusCardsFomDb());
    }

    public BonusCard getBonusCardId(int id) {
        String sql = "" +
                "SELECT *" +
                "FROM bonus_card " +
                "WHERE id = '" + id + "'";
        List<BonusCard> users = jdbcTemplate.query(sql, mapBonusCardsFomDb());
        if (users.size() > 0) {
            return users.get(0);
        }
        return null;
    }

    int inserBonusCard(BonusCard card) {
        BonusCard dbCard = this.getBonusCardId(card.getPk());

        if (dbCard == null) {
            String sql = "" +
                    "INSERT INTO bonus_card (" +
                    "number, " +
                    "good_thru_month, " +
                    "good_thru_year, " +
                    "blocked, " +
                    "expired, " +
                    "holderName, " +
                    "user_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            return jdbcTemplate.update(
                    sql,
                    card.getNumber(),
                    card.getGoodThruMonth(),
                    card.getGoodThruYear(),
                    card.isBlocked(),
                    card.isExpired(),
                    card.getHolderName(),
                    card.getUserId()
            );
        } else {
            String sql = "UPDATE bonus_card" +
                    "SET" +
                    " number = " + card.getNumber() +
                    ", good_thru_month = " + card.getGoodThruMonth() +
                    ", good_thru_year = " + card.getGoodThruYear() +
                    ", blocked = " + card.isBlocked() +
                    ", expired = " + card.isExpired() +
                    ", holderName = " + card.getHolderName() +
                    ", user_id = " + card.getUserId() +
                    "WHERE id = " + card.getPk();
            return jdbcTemplate.update(sql);
        }
    }
    private RowMapper<BonusCard> mapBonusCardsFomDb() {
        return (resultSet, i) -> {
            Integer id = resultSet.getInt("id");
            return new pvp.api.bonuscrads.BonusCard(
                    id,
                    resultSet.getString("number"),
                    resultSet.getString("holderName"),
                    resultSet.getInt("good_thru_month"),
                    resultSet.getInt("good_thru_year"),
                    resultSet.getBoolean("blocked"),
                    resultSet.getBoolean("expired"),
                    resultSet.getInt("user_id")
            );
        };
    }
}
