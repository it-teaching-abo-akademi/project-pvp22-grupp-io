package pvp.models.abstractModels;

public abstract class PkModel implements pvp.models.interfaces.PkModel {
    protected Integer pk;

    public PkModel(Integer pk) {
        this.pk = pk;
    }
    public PkModel() {this.pk = null;}

    @Override
    public Integer getPk() {
        return pk;
    }
}
