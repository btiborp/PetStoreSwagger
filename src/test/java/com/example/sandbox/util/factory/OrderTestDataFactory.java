package com.example.sandbox.util.factory;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class OrderTestDataFactory {

    public static long generateUniqueOrderId() {
        return ThreadLocalRandom.current().nextLong(100000, 999999);
    }

    public static String generateIsoDateNow() {
        return DateTimeFormatter.ISO_INSTANT.format(Instant.now());
    }

    public static Map<String, Object> createValidOrder(long orderId) {
        return Map.of(
            "id", orderId,
            "petId", 1,
            "quantity", 2,
            "shipDate", generateIsoDateNow(),
            "status", "placed",
            "complete", true
        );
    }

    // negativ esetekhez adat
    public static Map<String, Object> createOrderMissingPetId(long orderId) {
        return Map.of(
            "id", orderId,
            "quantity", 2,
            "shipDate", generateIsoDateNow(),
            "status", "placed",
            "complete", true
        );
    }

    public static Map<String, Object> createOrderWithNegativeQuantity(long orderId) {
        return Map.of(
            "id", orderId,
            "petId", 1,
            "quantity", -5,
            "shipDate", generateIsoDateNow(),
            "status", "placed",
            "complete", true
        );
    }

    public static Map<String, Object> createOrderWithInvalidStatusType(long orderId) {
        return Map.of(
            "id", orderId,
            "petId", 1,
            "quantity", 1,
            "shipDate", generateIsoDateNow(),
            "status", 123,  // ❌ numerikus, nem string
            "complete", true
        );
    }

    public static Map<String, Object> createEmptyOrder() {
        return Map.of(); // teljesen üres rendelés
    }

}
