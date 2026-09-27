package fr.miage.orders;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OrderListenerTest {
    private final OrderListener listener = new OrderListener(JsonMapper.builder().findAndAddModules().build());
    @Test void rejectsInvalidContract() {
        assertThatThrownBy(() -> listener.receive("{\"schemaVersion\":99}"))
            .isInstanceOf(IllegalArgumentException.class);
    }
    @Test void rejectsMalformedJson() {
        assertThatThrownBy(() -> listener.receive("not-json"))
            .isInstanceOf(com.fasterxml.jackson.core.JsonProcessingException.class);
    }
}
