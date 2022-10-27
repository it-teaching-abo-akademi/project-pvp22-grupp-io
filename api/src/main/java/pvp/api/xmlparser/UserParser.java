package pvp.api.xmlparser;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import pvp.api.bonuscrads.BonusCardService;
import pvp.api.user.UserService;
import pvp.models.Sex;
import pvp.models.interfaces.BonusCard;
import pvp.models.interfaces.User;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserParser extends DefaultHandler {
    private StringBuilder currentValue = new StringBuilder();
    List<User> result;
    User currentUser;
    BonusCard currentBonusCard;

    private final UserService userService;
    private final BonusCardService bonusCardService;

    public UserParser(UserService userService, BonusCardService bonusCardService) {
        this.bonusCardService = bonusCardService;
        this.userService = userService;
    }

    public List<User> getResult() {
        return result;
    }

    @Override
    public void startDocument() {
        result = new ArrayList<User>();
    }

    @Override
    public void startElement(
            String uri,
            String localName,
            String qName,
            Attributes attributes) {

        // reset the tag value
        currentValue.setLength(0);

        if (qName.equalsIgnoreCase("customer")) {
            String id = attributes.getValue("customerNo");
            currentUser = userService.getUserById(Integer.parseInt(id));
            if (currentUser == null) {
                currentUser = new pvp.api.user.User(
                        Integer.parseInt(id),
                        null,
                        null,
                        null,
                        null,
                        new HashSet<BonusCard>(),
                        0
                );
            }
        }
        if (qName.equalsIgnoreCase("bonusCard")) {
            int id = Integer.parseInt(attributes.getValue("id"));
            currentBonusCard = bonusCardService.getBonusCardId(id);
            if (currentBonusCard == null) {
                currentBonusCard = new pvp.api.bonuscrads.BonusCard(
                        id,
                        "",
                        "",
                        0,
                        0,
                        false,
                        false,
                        currentUser.getPk()
                );
            }
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("firstName")) {
            currentUser.setFirstName(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("lastName")) {
            currentUser.setLastName(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("birthDate")) {
            ZonedDateTime birthday = ZonedDateTime.parse(currentValue.toString());
            currentUser.setBirthDay(birthday);
        }
        if (qName.equalsIgnoreCase("number")) {
            currentBonusCard.setNumber(currentValue.toString());
        }
        if (qName.equalsIgnoreCase("goodThruMonth")) {
            currentBonusCard.setGoodThruMonth(Integer.parseInt(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("goodThruYear")) {
            currentBonusCard.setGoodThruYear(Integer.parseInt(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("blocked")) {
            currentBonusCard.setIsBlocked(Boolean.valueOf(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("expired")) {
            currentBonusCard.setIsExpired(Boolean.valueOf(currentValue.toString()));
        }
        if (qName.equalsIgnoreCase("holderName")) {
            currentBonusCard.setHolderName(currentValue.toString());
        }

        if (qName.equalsIgnoreCase("bonusCard")) {
            Set<BonusCard> cards = currentUser.getBonusCards();
            cards.add(currentBonusCard);
            currentUser.setBonusCards(cards);
        }
        if (qName.equalsIgnoreCase("customer")) {
            result.add(currentUser);
            userService.addNewUser((pvp.api.user.User) currentUser);
        }
    }

    public void characters(char ch[], int start, int length) {
        currentValue.append(ch, start, length);
    }
}
