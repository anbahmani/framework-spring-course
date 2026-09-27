package fr.miage.debut;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest(properties={"demo.send=false", "spring.jms.listener.auto-startup=false"})
class MessageTest {
    @Autowired NotificationSender sender;
    @Autowired JmsTemplate jms;
    @Test void transportsText() {
        jms.setReceiveTimeout(5000);
        sender.send("Bienvenue Ana");
        assertEquals("Bienvenue Ana", jms.receiveAndConvert("notifications"));
    }
}
