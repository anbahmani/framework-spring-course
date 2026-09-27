package fr.miage.debut;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
@Component
public class ClientRunner implements CommandLineRunner {
    private final DirectoryClient directory;
    public ClientRunner(DirectoryClient directory) { this.directory = directory; }
    @Override
    public void run(String... args) {
        try {
            UserView[] users = directory.list();
            if (users != null) {
                System.out.println("Utilisateurs reçus : " + users.length);
                for (UserView user : users) System.out.println(user.id() + " : " + user.name());
            }
        } catch (RestClientException error) {
            System.out.println("Impossible de lire l'annuaire : vérifier le serveur et son adresse.");
        }
    }
}
