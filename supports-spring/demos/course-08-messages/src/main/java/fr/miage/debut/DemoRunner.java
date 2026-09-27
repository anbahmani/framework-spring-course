package fr.miage.debut;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
@Component
@ConditionalOnProperty(name="demo.send", havingValue="true", matchIfMissing=true)
public class DemoRunner implements CommandLineRunner {
    private final NotificationSender sender;
    public DemoRunner(NotificationSender sender) { this.sender = sender; }
    @Override
    public void run(String... args) {
        sender.send("Bienvenue Ana");
        System.out.println("Demande envoyée");
    }
}
