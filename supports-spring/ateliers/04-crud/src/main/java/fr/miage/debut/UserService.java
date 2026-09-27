package fr.miage.debut;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private final Map<Long, UserView> users = new LinkedHashMap<>();
    private long nextId = 1;
    public synchronized List<UserView> list() { return new ArrayList<>(users.values()); }
    public synchronized UserView get(long id) {
        UserView user = users.get(id);
        if (user == null) throw new UserNotFound();
        return user;
    }
    public synchronized UserView create(String name) {
        checkName(name);
        UserView user = new UserView(nextId++, name.strip());
        users.put(user.id(), user);
        return user;
    }
    public synchronized UserView update(long id, String name) {
        checkName(name);
        get(id);
        UserView user = new UserView(id, name.strip());
        users.put(id, user);
        return user;
    }
    public synchronized void delete(long id) {
        get(id);
        users.remove(id);
    }
    private void checkName(String name) {
        if (name == null || name.isBlank()) throw new InvalidName();
    }
}
