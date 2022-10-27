package pvp.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pvp.models.abstractModels.PkModel;
import pvp.models.interfaces.BonusCard;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends PkModel implements pvp.models.interfaces.User {
    private String first_name;
    private String last_name;
    private Sex sex;
    private ZonedDateTime birthday;
    private Set<BonusCard> bonus_cards;
    private int bonus_points;

    public User(
            Integer pk, String f_name, String l_name,
            Sex sex, ZonedDateTime birthday, Set<BonusCard> bonus_cards,
            Integer bonus_points
    ) {
        super(pk);
        this.first_name = f_name;
        this.last_name = l_name;
        this.sex = sex;
        this.birthday = birthday;
        this.bonus_cards = bonus_cards;
        this.bonus_points = bonus_points;
    }

    @Override
    public void setFirstName(String first_name) {
        this.first_name = first_name;
    }

    @Override
    public void setLastName(String last_name) {
        this.last_name = last_name;
    }

    @Override
    public void setBirthDay(ZonedDateTime birthday) {
        this.birthday = birthday;
    }

    @Override
    public void setSex(Sex sex) {
        this.sex = sex;
    }

    @Override
    public void setBonusCards(Set<BonusCard> cards) {
        this.bonus_cards = cards;
    }

    @Override
    public void setBonusPoints(int bonus_points) {
        this.bonus_points = bonus_points;
    }

    @Override
    public String getFirstName() {
        return this.first_name;
    }

    @Override
    public String getLastName() {
        return this.last_name;
    }

    @Override
    public ZonedDateTime getBirthDay() {
        return this.birthday;
    }

    @Override
    public Sex getSex() {
        return this.sex;
    }

    @Override
    public Set<BonusCard> getBonusCards() {
        return this.bonus_cards;
    }

    @Override
    public int getBonusPoints() {
        return this.bonus_points;
    }

    @Override
    public JSONObject getJsonOfObject() {
        JSONObject json = new JSONObject();
        ZonedDateTime birthDay = this.getBirthDay();
        String bday = "";
        if (birthDay != null) {
            bday = birthDay.toString();
        }
        json.put("pk", this.getPk());
        json.put("firstName", this.getFirstName());
        json.put("lastName", this.getLastName());
        json.put("sex", this.getSex().name());
        json.put("bonusPoints", this.getBonusPoints());
        json.put("birthday", bday);
        JSONArray bonusCards = new JSONArray();
        this.bonus_cards.forEach(bonusCard -> {
            bonusCard.setUser(null);
            bonusCard.setUserId(this.getPk());
            bonusCards.put(bonusCard.getJsonOfObject());
        });
        json.put("bonus_cards", bonusCards);
        return json;
    }

    public static pvp.models.interfaces.User getObjectFromJson(JSONObject json) {
        Integer bPoints;
        ZonedDateTime birthDay;
        try {
            bPoints = json.getInt("bonusPoints");
        } catch (JSONException e) {
            bPoints = Integer.parseInt(json.getString("bonusPoints"));
        }
        try {
            String birthday = json.getString("birthday");
            if (birthday.equals("")) {
                throw new JSONException("Fail");
            }
            birthDay = ZonedDateTime.parse(birthday);
        } catch (JSONException e) {
            birthDay = null;
        }

        pvp.models.interfaces.User user = new User(
                json.getInt("pk"),
                json.getString("firstName"),
                json.getString("lastName"),
                Sex.valueOf(json.getString("sex")),
                birthDay,
                new HashSet<BonusCard>(),
                bPoints
        );

        if (json.has("bonus_cards")) {
            json.getJSONArray("bonus_cards").forEach(jsonObject -> {
                Set<BonusCard> cards = user.getBonusCards();
                BonusCard bonusCard = pvp.models.BonusCard.getObjectFromJson((JSONObject) jsonObject);
                bonusCard.setUser(user);
                cards.add(bonusCard);
                user.setBonusCards(cards);
            });
        }
        return user;
    }
}
