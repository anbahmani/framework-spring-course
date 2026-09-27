package fr.miage.debut;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
@Component
public class NotificationReceiver {
    @JmsListener(destination="notifications")
    public void receive(String message) { System.out.println("RECU : " + message); }
}
