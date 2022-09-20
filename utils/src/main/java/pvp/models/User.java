package pvp.models;

import pvp.models.abstractModels.PkModel;

import java.util.UUID;

public class User extends PkModel implements pvp.models.interfaces.User {
    private String name;
    private UUID customerReference;

    public User(Integer pk, UUID customerReference, String name) {
        super(pk);
        this.name = name;
        this.customerReference = customerReference;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getCustomerReference() {
        return this.customerReference;
    }
}
