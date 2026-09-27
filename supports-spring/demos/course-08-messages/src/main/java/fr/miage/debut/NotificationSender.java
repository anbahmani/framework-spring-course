package fr.miage.debut;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
@Service
public class NotificationSender {
    private final JmsTemplate jms;
    public NotificationSender(JmsTemplate jms) { this.jms = jms; }
    public void send(String message) { jms.convertAndSend("notifications", message); }
}
