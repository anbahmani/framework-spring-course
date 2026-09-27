package fr.miage.orders;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoProducer {
    @Bean
    @ConditionalOnProperty(name = "demo.produce", havingValue = "true")
    ApplicationRunner publishExamples(OrderPublisher publisher) {
        return args -> {
            for (long id = 1; id <= 5; id++) publisher.send(OrderEvent.created(id));
        };
    }
}
