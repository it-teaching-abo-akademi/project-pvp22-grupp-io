package pvp.models.abstractModels;

public abstract class PkModel implements pvp.models.interfaces.PkModel {
    protected String pk;

    public PkModel(String pk) {
        this.pk = pk;
    }

    @Override
    public String getPk() {
        return pk;
    }
}
