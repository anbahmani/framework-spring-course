package fr.miage.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.core.JmsTemplate;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {"demo.produce=false", "spring.jms.listener.auto-startup=false"})
class OrderTransportTest {
    @Autowired OrderPublisher publisher;
    @Autowired JmsTemplate jms;
    @Autowired ObjectMapper mapper;
    @Test void transportsJsonThroughEmbeddedBroker() throws Exception {
        OrderEvent expected = OrderEvent.created(42);
        jms.setReceiveTimeout(5000);
        publisher.send(expected);
        Object body = jms.receiveAndConvert("OrdersQueue");
        assertThat(body).isInstanceOf(String.class);
        assertThat(mapper.readValue((String) body, OrderEvent.class)).isEqualTo(expected);
    }
}
