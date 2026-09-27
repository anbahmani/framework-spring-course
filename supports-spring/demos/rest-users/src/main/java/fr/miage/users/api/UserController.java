package fr.miage.users.api;

import fr.miage.users.application.UserService;
import fr.miage.users.application.UserView;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService service;
    public UserController(UserService service) { this.service = service; }
    @GetMapping
    public List<UserView> list() { return service.list(); }
    @GetMapping("/{id}")
    public UserView get(@PathVariable("id") long id) { return service.get(id); }
    @PostMapping
    public ResponseEntity<UserView> create(@Valid @RequestBody UserRequest request) {
        UserView user = service.create(request.name(), request.email());
        return ResponseEntity.created(URI.create("/api/users/" + user.id())).body(user);
    }
    @PutMapping("/{id}")
    public UserView update(@PathVariable("id") long id, @Valid @RequestBody UserRequest request) {
        return service.update(id, request.name(), request.email());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
