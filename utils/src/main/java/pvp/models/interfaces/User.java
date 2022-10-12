package pvp.models.interfaces;

import java.util.UUID;

public interface User extends PkModel {
    public void setName(String name);
    public String getName();
    public UUID getCustomerReference();
}
