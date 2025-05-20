package com.example.sandbox.util.factory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class UserTestDataFactory {

    public static String generateRandomUsername() {
        return "user" + ThreadLocalRandom.current().nextInt(10000, 99999);
    }

    public static Map<String, Object> createValidUser(String username) {
        return new HashMap<>() {{
            put("id", ThreadLocalRandom.current().nextLong(1000, 9999));
            put("username", username);
            put("firstName", "Test");
            put("lastName", "User");
            put("email", username + "@example.com");
            put("password", "test123");
            put("phone", "123456789");
            put("userStatus", 1);
        }};
    }

    public static Map<String, Object> createUpdatedUser(String username) {
        return new HashMap<>() {{
            put("id", ThreadLocalRandom.current().nextLong(1000, 9999));
            put("username", username);
            put("firstName", "Updated");
            put("lastName", "Name");
            put("email", username + "@updated.com");
            put("password", "updatedpass");
            put("phone", "987654321");
            put("userStatus", 0);
        }};
    }

    // negativ tesztekhez adat
    public static Map<String, Object> createUserMissingUsername() {
        return new HashMap<>() {{
            put("id", ThreadLocalRandom.current().nextLong(1000, 9999));
            put("firstName", "NoUser");
            put("lastName", "Name");
            put("email", "nobody@example.com");
            put("password", "12345");
            put("phone", "123456789");
            put("userStatus", 1);
        }};
    }

    public static Map<String, Object> createUserWithNumericEmail(String username) {
        return new HashMap<>() {{
            put("id", ThreadLocalRandom.current().nextLong(1000, 9999));
            put("username", username);
            put("firstName", "Num");
            put("lastName", "Email");
            put("email", 12345); // ❌ nem string
            put("password", "12345");
            put("phone", "0000000");
            put("userStatus", 1);
        }};
    }

    public static Map<String, Object> createEmptyUser() {
        return Map.of();
    }

    // tömeges user létrehozásahoz tesztadat
    public static List<Map<String, Object>> generateUserList(int count) {
        List<Map<String, Object>> users = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String username = generateRandomUsername();
                users.add(createValidUser(username));
            }
        return users;
    }

    // negatív tesztadatok a /user/createWithArray és /user/createWithList végpontokhoz
    public static List<Map<String, Object>> createUserListWithMissingField() {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(createValidUser(generateRandomUsername()));
        Map<String, Object> invalidUser = new HashMap<>();
        invalidUser.put("id", 123);
        invalidUser.put("email", "invalid@example.com");
        users.add(invalidUser); // ❌ nincs username
        return users;
    }

    public static List<Map<String, Object>> createUserListWithWrongType() {
        List<Map<String, Object>> users = new ArrayList<>();
        Map<String, Object> badUser = createValidUser(generateRandomUsername());
        badUser.put("email", 12345); // ❌ email nem string
        users.add(badUser);
        return users;
    }

}
