package fr.miage.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class OrderListener {
    private static final Logger LOG = LoggerFactory.getLogger(OrderListener.class);
    private final ObjectMapper mapper;
    public OrderListener(ObjectMapper mapper) { this.mapper = mapper; }
    @JmsListener(destination = "${demo.queue}")
    public void receive(String body) throws JsonProcessingException {
        OrderEvent event = mapper.readValue(body, OrderEvent.class);
        if (event == null || event.eventId() == null || event.createdAt() == null
                || event.schemaVersion() != 1 || !"ORDER".equals(event.type()) || event.orderId() <= 0) {
            throw new IllegalArgumentException("Événement ORDER invalide");
        }
        LOG.info("Reçu eventId={} orderId={}", event.eventId(), event.orderId());
        // Ce laboratoire ne réalise aucun effet métier persistant ni dédoublonnage.
    }
}
