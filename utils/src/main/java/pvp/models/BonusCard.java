package pvp.models;

import org.json.JSONObject;
import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.User;

public class BonusCard extends PkModel implements pvp.models.interfaces.BonusCard {
    private String number;
    private String name;
    private int good_thr_month;
    private int good_thr_year;
    private boolean blocked;
    private boolean expired;
    private User user;
    private Integer user_id;

    public BonusCard(
            Integer pk, String number, String name,
            int thr_month, int thr_year,
            boolean blocked, boolean expired, User user){
        super(pk);
        this.number = number;
        this.name = name;
        this.good_thr_month = thr_month;
        this.good_thr_year = thr_year;
        this.blocked = blocked;
        this.expired = expired;
        this.user = user;
        this.user_id = user.getPk();
    }

    public BonusCard(
            Integer pk, String number, String name,
            int thr_month, int thr_year,
            boolean blocked, boolean expired, Integer user_id){
        super(pk);
        this.number = number;
        this.name = name;
        this.good_thr_month = thr_month;
        this.good_thr_year = thr_year;
        this.blocked = blocked;
        this.expired = expired;
        this.user_id = user_id;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public void setHolderName(String name) {
        this.name = name;
    }

    @Override
    public void setGoodThruMonth(int good_thr_month) {
        this.good_thr_month = good_thr_month;
    }

    @Override
    public void setGoodThruYear(int good_thr_year) {
        this.good_thr_year = good_thr_year;
    }

    @Override
    public void setIsBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    @Override
    public void setIsExpired(boolean expired) {
        this.expired = expired;
    }

    @Override
    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public void setUserId(Integer id) {
        this.user_id = id;
    }

    @Override
    public String getNumber() {
        return this.number;
    }

    @Override
    public String getHolderName() {
        return this.name;
    }

    @Override
    public int getGoodThruMonth() {
        return this.good_thr_month;
    }

    @Override
    public int getGoodThruYear() {
        return this.good_thr_year;
    }

    @Override
    public boolean isBlocked() {
        return this.blocked;
    }

    @Override
    public boolean isExpired() {
        return this.expired;
    }

    @Override
    public User getUser() {
        return this.user;
    }

    @Override
    public Integer getUserId() {
        return this.user_id;
    }

    @Override
    public JSONObject getJsonOfObject() {
        JSONObject json = new JSONObject();
        json.put("pk", this.getPk());
        json.put("isExpired", this.isExpired());
        json.put("isBlocked", this.isBlocked());
        json.put("goodThruYear", this.getGoodThruYear());
        json.put("goodThruMonth", this.getGoodThruMonth());
        json.put("number", this.getNumber());
        json.put("holderName", this.getHolderName());
        if (this.user != null) {
            json.put("user", this.user.getJsonOfObject());
        } else {
            json.put("user_id", this.getUserId());
        }
        return json;
    }

    public static pvp.models.interfaces.BonusCard getObjectFromJson(JSONObject json) {
        User user = null;
        Integer user_id = null;
        if (json.has("user")) {
            user = pvp.models.User.getObjectFromJson(json.getJSONObject("user"));
            return new BonusCard(
                    json.getInt("pk"),
                    json.getString("number"),
                    json.getString("holderName"),
                    json.getInt("goodThruMonth"),
                    json.getInt("goodThruYear"),
                    json.getBoolean("isBlocked"),
                    json.getBoolean("isExpired"),
                    user
            );
        } else {
            return new BonusCard(
                    json.getInt("pk"),
                    json.getString("number"),
                    json.getString("holderName"),
                    json.getInt("goodThruMonth"),
                    json.getInt("goodThruYear"),
                    json.getBoolean("isBlocked"),
                    json.getBoolean("isExpired"),
                    json.getInt("user_id")
            );
        }
    }
}
