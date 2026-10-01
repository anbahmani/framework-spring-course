package fr.miage.mcp;

import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DirectoryService {
    private final List<UserSummary> users = List.of(
            new UserSummary(1, "Ana"),
            new UserSummary(2, "Léa"));

    public String findUserById(long id) {
        return users.stream()
                .filter(user -> user.id() == id)
                .findFirst()
                .map(user -> "Utilisateur #" + user.id() + " : " + user.name())
                .orElse("Aucun utilisateur avec l’identifiant " + id);
    }

    private record UserSummary(long id, String name) {}
}
