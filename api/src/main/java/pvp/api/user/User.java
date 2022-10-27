package pvp.api.user;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import pvp.models.Sex;
import pvp.models.interfaces.BonusCard;

@EntityScan
public class User extends pvp.models.User implements Serializable {

    /**
     * User()
     * Extends the base User with JsonProperties to enable the serialization/deserialization functionality.
     */
    public User(
            @JsonProperty("pk") int pk,
            @JsonProperty("first_name") String first_name,
            @JsonProperty("last_name") String last_name,
            @JsonProperty("sex") Sex sex,
            @JsonProperty("birthday") ZonedDateTime birthday,
            @JsonProperty("bonus_cards") Set<BonusCard> bonus_cards,
            @JsonProperty("bonus_points") Integer bonus_points
    ) {
        super(pk, first_name, last_name, sex, birthday, bonus_cards, bonus_points);
    }
}
