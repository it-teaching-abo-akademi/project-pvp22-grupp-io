package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import pvp.api.bonuscrads.BonusCardAccessService;
import pvp.api.bonuscrads.BonusCardService;
import pvp.api.xmlparser.ProductParser;
import pvp.api.xmlparser.UserParser;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDataAccessService userDataAccessService;

    @Autowired
    public UserService(UserDataAccessService userDataAccessService, BonusCardService bonusCardService) {
        this.userDataAccessService = userDataAccessService;

        int index = -1;
        int countOfNone = 0;
        while (countOfNone < 20) {
            try {
                index = index + 1;
                URL url = new URL("http://localhost:9004/rest/findByCustomerNo/" + index);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                int statusCode = httpURLConnection.getResponseCode();
                if (statusCode != 200) {
                    countOfNone = countOfNone + 1;
                    continue;
                }

                BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                SAXParserFactory factory = SAXParserFactory.newInstance();

                SAXParser saxParser = factory.newSAXParser();
                UserParser handler = new UserParser(this, bonusCardService);
                saxParser.parse(new ByteArrayInputStream(response.toString().getBytes()), handler);
                List<pvp.models.interfaces.User> result = handler.getResult();
                result.stream().forEach(user -> {
                    user.getBonusCards().stream().forEach(card -> {
                        card.setUser(user);
                        bonusCardService.addNewBonusCard(card);
                    });
                });
                countOfNone = countOfNone + 1;
            } catch (ParserConfigurationException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    List<User> getAllUsers() {
        return userDataAccessService.selectAllUsers();
    }

    public User getUserById(int id) {return userDataAccessService.getUserById(id);}

    User findBySearch(String search) {
        return userDataAccessService.getUserBySearch(search);
    }

    User findByCard(String number, Integer month, Integer year){
        return userDataAccessService.getUserByCard(number, month, year);
    }

    public void addNewUser(User user) {
        userDataAccessService.insertUser(user);
    }
}
