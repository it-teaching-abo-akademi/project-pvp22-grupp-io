package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    private final UserDataAccessService userDataAccessService;

    @Autowired
    public UserService(UserDataAccessService userDataAccessService) {
        this.userDataAccessService = userDataAccessService;
    }

    List<User> getAllUsers() {
        return userDataAccessService.selectAllUsers();
    }

    User findByReference(UUID reference) {
        return userDataAccessService.getUserByReference(reference);
    }

    void addNewUser(User user) {
        userDataAccessService.insertUser(user);
    }
}
