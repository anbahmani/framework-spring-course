package fr.miage.debut;
import org.springframework.web.bind.annotation.*;
@RestController
public class UserController {
    @GetMapping("/hello")
    public String hello(@RequestParam(name="name", defaultValue="tout le monde") String name) {
        return "Bonjour " + name;
    }
    @GetMapping("/users/{id}")
    public UserView user(@PathVariable("id") long id) {
        return new UserView(id, "Ana");
    }
}
