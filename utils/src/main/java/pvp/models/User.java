package pvp.models;

import pvp.models.abstractModels.PkModel;

import java.util.UUID;

public class User extends PkModel {
    private String name;
    private UUID customerReference;

    public User(String pk, UUID customerReference, String name) {
        super(pk);
        if (customerReference == null) {
            customerReference = UUID.randomUUID();
        }
        this.customerReference = customerReference;
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public UUID getCustomerReference() {
        return this.customerReference;
    }
}
