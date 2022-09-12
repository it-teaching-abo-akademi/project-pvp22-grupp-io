package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        addNewUser(null, user);
    }

    void addNewUser(UUID userId, User user) {
        UUID newUserId = Optional.ofNullable(userId)
                .orElse(UUID.randomUUID());


        userDataAccessService.insertUser(newUserId, user);
    }
}
