package fr.miage.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderPublisher {
    private final JmsTemplate jms;
    private final ObjectMapper mapper;
    private final String queue;
    public OrderPublisher(JmsTemplate jms, ObjectMapper mapper, @Value("${demo.queue}") String queue) {
        this.jms = jms; this.mapper = mapper; this.queue = queue;
    }
    public void send(OrderEvent event) throws JsonProcessingException {
        jms.convertAndSend(queue, mapper.writeValueAsString(event));
    }
}
