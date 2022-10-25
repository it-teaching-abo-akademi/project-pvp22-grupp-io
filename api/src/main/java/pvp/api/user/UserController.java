package pvp.api.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/{customer_reference}")
    public User getNoteById(@PathVariable(value = "customer_reference") UUID customerReference){
        return userService.findByReference(customerReference);
    }

    @PostMapping
    public void addNewUser(@RequestBody User user) {
        this.userService.addNewUser(user);
    }

}
