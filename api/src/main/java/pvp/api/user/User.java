package pvp.api.user;

import java.io.Serializable;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan
public class User extends pvp.models.User implements Serializable {

    /**
     * User()
     * Extends the base User with JsonProperties to enable the serialization/deserialization functionality.
     */
    public User(
            @JsonProperty("pk") int pk,
            @JsonProperty("customer_reference") UUID customerReference,
            @JsonProperty("name") String name
    ) {
        super(pk, customerReference, name);
    }
}
