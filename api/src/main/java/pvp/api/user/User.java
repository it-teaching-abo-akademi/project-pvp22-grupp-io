package pvp.api.user;

import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.lang.NonNull;

@EntityScan
public class User extends pvp.models.User {
    public User(
            @JsonProperty("pk") String pk,
            @JsonProperty("customer_reference") UUID customerReference,
            @JsonProperty("name") String name
    ) {
        super(pk, customerReference, name);
    }
}
