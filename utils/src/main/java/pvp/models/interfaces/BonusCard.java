package pvp.models.interfaces;

import java.net.InterfaceAddress;

public interface BonusCard extends PkModel, JSONSerializable {
    // Getters
    public void setNumber(String number);
    public void setHolderName(String name);
    public void setGoodThruMonth(int good_thr_month);
    public void setGoodThruYear(int good_thr_year);
    public void setIsBlocked(boolean blocked);
    public void setIsExpired(boolean expired);
    public void setUser(User user);
    public void setUserId(Integer id);

    // Setters
    public String getNumber();
    public String getHolderName();
    public int getGoodThruMonth();
    public int getGoodThruYear();
    public boolean isBlocked();
    public boolean isExpired();
    public User getUser();
    public Integer getUserId();
}
