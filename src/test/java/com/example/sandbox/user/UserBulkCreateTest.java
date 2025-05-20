package com.example.sandbox.user;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.example.sandbox.util.factory.UserTestDataFactory.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;

public class UserBulkCreateTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="user-createWithArray")
    public void testCreateUsersWithArray() {
        List<Map<String, Object>> users = generateUserList(3);

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(users)
        .when()
            .post("/user/createWithArray")
        .then()
            .statusCode(200)
            .body("message", equalTo("ok"));
    }

    @Test(enabled = true,groups = {SMOKE},description ="user-createWithList")
    public void testCreateUsersWithList() {
        List<Map<String, Object>> users = generateUserList(3);

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(users)
        .when()
            .post("/user/createWithList")
        .then()
            .statusCode(200)
            .body("message", equalTo("ok"));
    }
}
