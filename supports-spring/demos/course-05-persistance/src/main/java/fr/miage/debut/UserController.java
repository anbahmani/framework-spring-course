package fr.miage.debut;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping
    public List<UserView> list() { return service.list(); }
    @GetMapping("/{id}")
    public UserView get(@PathVariable("id") long id) { return service.get(id); }
    @PostMapping
    public ResponseEntity<UserView> create(@RequestBody UserRequest request) {
        UserView created = service.create(request.name());
        return ResponseEntity.created(URI.create("/users/" + created.id())).body(created);
    }
    @PutMapping("/{id}")
    public UserView update(@PathVariable("id") long id, @RequestBody UserRequest request) {
        return service.update(id, request.name());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
