package fr.miage.debut;
import org.springframework.web.bind.annotation.*;
@RestController
public class HelloController {
    private final GreetingService service;
    public HelloController(GreetingService service) { this.service = service; }
    @GetMapping("/hello")
    public String hello(@RequestParam(name="name", defaultValue="tout le monde") String name) {
        return service.greet(name);
    }
}
