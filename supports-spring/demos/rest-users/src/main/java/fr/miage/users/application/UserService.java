package fr.miage.users.application;

import fr.miage.users.domain.UserNotFound;
import fr.miage.users.persistence.UserEntity;
import fr.miage.users.persistence.UserRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository users;
    public UserService(UserRepository users) { this.users = users; }
    public List<UserView> list() {
        return users.findAll(Sort.by("id")).stream().map(UserView::from).toList();
    }
    public UserView get(long id) { return UserView.from(required(id)); }
    @Transactional
    public UserView create(String name, String email) {
        return UserView.from(users.save(new UserEntity(name, email)));
    }
    @Transactional
    public UserView update(long id, String name, String email) {
        UserEntity user = required(id);
        user.rename(name, email); // dirty checking : entité gérée
        return UserView.from(user);
    }
    @Transactional
    public void delete(long id) { users.delete(required(id)); }
    private UserEntity required(long id) {
        return users.findById(id).orElseThrow(() -> new UserNotFound(id));
    }
}
