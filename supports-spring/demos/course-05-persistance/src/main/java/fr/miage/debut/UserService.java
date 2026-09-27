package fr.miage.debut;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final UserRepository repository;
    public UserService(UserRepository repository) { this.repository = repository; }
    public List<UserView> list() {
        List<UserView> result = new ArrayList<>();
        for (UserEntity entity : repository.findAll(Sort.by("id"))) {
            result.add(view(entity));
        }
        return result;
    }
    public UserView get(long id) { return view(required(id)); }
    public UserView create(String name) {
        checkName(name);
        return view(repository.save(new UserEntity(name.strip())));
    }
    public UserView update(long id, String name) {
        checkName(name);
        UserEntity entity = required(id);
        entity.setName(name.strip());
        return view(repository.save(entity));
    }
    public void delete(long id) { repository.delete(required(id)); }
    private UserEntity required(long id) {
        return repository.findById(id).orElseThrow(UserNotFound::new);
    }
    private UserView view(UserEntity entity) {
        return new UserView(entity.getId(), entity.getName());
    }
    private void checkName(String name) {
        if (name == null || name.isBlank()) throw new InvalidName();
    }
}
