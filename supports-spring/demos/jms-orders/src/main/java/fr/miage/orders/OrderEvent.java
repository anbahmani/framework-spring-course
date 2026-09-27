package fr.miage.orders;

import java.time.Instant;
import java.util.UUID;

public record OrderEvent(UUID eventId, int schemaVersion, String type, long orderId, Instant createdAt) {
    public static OrderEvent created(long orderId) {
        return new OrderEvent(UUID.randomUUID(), 1, "ORDER", orderId, Instant.now());
    }
}
