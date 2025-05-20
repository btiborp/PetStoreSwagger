package com.example.sandbox.util.helpers;

import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class PetApiHelper {

    /**
     * Lekéri az első olyan pet ID-ját, amelynek státusza "available".
     * Kezeli, ha a válasz objektum vagy lista.
     */
    public static String getFirstAvailablePetId(Response response) {
        assertEquals(response.getStatusCode(), 200, "A válasz nem 200 OK");

        Object raw = response.jsonPath().get("");
        Long id;

        if (raw instanceof List) {
            List<Map<String, Object>> pets = response.jsonPath().getList("$");
            assertFalse(pets.isEmpty(), "A lista üres.");
            id = Long.parseLong(pets.get(0).get("id").toString());
        } else if (raw instanceof Map) {
            id = response.jsonPath().getLong("id");
        } else {
            throw new IllegalStateException("Nem várt válaszstruktúra.");
        }

        assertNotNull(id, "Nem sikerült ID-t kinyerni a válaszból.");
        return id.toString();
    }
}
