package pvp.api.bonuscrads;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import pvp.api.user.User;

@EntityScan
public class BonusCard extends pvp.models.BonusCard {
    public BonusCard(
            @JsonProperty("pk") int pk,
            @JsonProperty("number") String number,
            @JsonProperty("name") String name,
            @JsonProperty("thr_month") int thr_month,
            @JsonProperty("thr_year") int thr_year,
            @JsonProperty("blocked") boolean blocked,
            @JsonProperty("expired") boolean expired,
            @JsonProperty("user") User user
    ) {
        super(pk, number, name, thr_month, thr_year, blocked, expired, user);
    }
    public BonusCard(
            @JsonProperty("pk") int pk,
            @JsonProperty("number") String number,
            @JsonProperty("name") String name,
            @JsonProperty("thr_month") int thr_month,
            @JsonProperty("thr_year") int thr_year,
            @JsonProperty("blocked") boolean blocked,
            @JsonProperty("expired") boolean expired,
            @JsonProperty("user_id") Integer user_id
    ) {
        super(pk, number, name, thr_month, thr_year, blocked, expired, user_id);
    }
}
