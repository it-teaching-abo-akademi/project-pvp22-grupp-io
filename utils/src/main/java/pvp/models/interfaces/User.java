package pvp.models.interfaces;

import org.json.JSONObject;
import pvp.models.Sex;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Set;

public interface User extends PkModel, JSONSerializable {
    // Getters
    public void setFirstName(String first_name);
    public void setLastName(String last_name);
    public void setBirthDay(ZonedDateTime birthday);
    public void setSex(Sex sex);
    public void setBonusCards(Set<BonusCard> cards);
    public void setBonusPoints(int bonus_points);

    // Setters
    public String getFirstName();
    public String getLastName();
    public ZonedDateTime getBirthDay();
    public Sex getSex();
    public Set<BonusCard> getBonusCards();
    public int getBonusPoints();
}
