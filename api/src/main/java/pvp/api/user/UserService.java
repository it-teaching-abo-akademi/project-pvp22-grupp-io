package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import pvp.api.bonuscrads.BonusCardService;
import pvp.api.xmlparser.ProductParser;
import pvp.api.xmlparser.UserParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDataAccessService userDataAccessService;

    @Autowired
    public UserService(UserDataAccessService userDataAccessService, BonusCardService bonusCardService) {
        this.userDataAccessService = userDataAccessService;
        String test = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                "<customer customerNo=\"1\" optLockVersion=\"1\">\n" +
                "    <firstName>Last name</firstName>\n" +
                "    <lastName>First name</lastName>\n" +
                "    <birthDate>2022-10-26T00:00:00+03:00</birthDate>\n" +
                "    <address>\n" +
                "        <streetAddress>Sirkkalagatan 36</streetAddress>\n" +
                "        <postalCode>20700</postalCode>\n" +
                "        <postOffice>Turku</postOffice>\n" +
                "        <country>Finland</country>\n" +
                "    </address>\n" +
                "    <bonusCard id=\"2\" optLockVersion=\"1\">\n" +
                "        <number>1234567890</number>\n" +
                "        <goodThruMonth>14</goodThruMonth>\n" +
                "        <goodThruYear>2022</goodThruYear>\n" +
                "        <blocked>false</blocked>\n" +
                "        <expired>false</expired>\n" +
                "        <holderName>First last</holderName>\n" +
                "    </bonusCard>\n" +
                "    <sex>FEMALE</sex>\n" +
                "</customer>";
        SAXParserFactory factory = SAXParserFactory.newInstance();

        try {
            SAXParser saxParser = factory.newSAXParser();
            UserParser handler = new UserParser(this, bonusCardService);
            saxParser.parse(new ByteArrayInputStream(test.getBytes()), handler);
            List<pvp.models.interfaces.User> result = handler.getResult();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    List<User> getAllUsers() {
        return userDataAccessService.selectAllUsers();
    }

    public User getUserById(int id) {return userDataAccessService.getUserById(id);}

    User findByReference(UUID reference) {
        return userDataAccessService.getUserByReference(reference);
    }

    public void addNewUser(User user) {
        userDataAccessService.insertUser(user);
    }
}
