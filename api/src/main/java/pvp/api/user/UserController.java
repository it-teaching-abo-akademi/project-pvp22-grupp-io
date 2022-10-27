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

    @GetMapping("/{search}")
    public User getUserBySearch(@PathVariable(value = "search") String search){
        return userService.findBySearch(search);
    }

    @GetMapping("/{number}/{month}/{year}")
    public User getUserByCard(
            @PathVariable(value = "number") String number,
            @PathVariable(value = "month") Integer month,
            @PathVariable(value = "year") Integer year
    ){
        return userService.findByCard(number, month, year);
    }

    @PostMapping
    public void addNewUser(@RequestBody User user) {
        this.userService.addNewUser(user);
    }

}
