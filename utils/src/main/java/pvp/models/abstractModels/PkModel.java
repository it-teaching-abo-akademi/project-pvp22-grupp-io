package pvp.models.abstractModels;

import java.util.Optional;

public abstract class PkModel implements pvp.models.interfaces.PkModel {
    protected int pk;

    public PkModel(Integer pk) {
        this.pk = pk;
    }

    @Override
    public int getPk() {
        return pk;
    }
}
