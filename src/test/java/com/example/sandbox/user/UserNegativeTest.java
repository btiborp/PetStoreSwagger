package com.example.sandbox.user;

import org.testng.annotations.Test;

import java.util.Map;

import static com.example.sandbox.util.factory.UserTestDataFactory.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;

public class UserNegativeTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="user-MissingUsername")
    public void testMissingUsername_ShouldFail() {
        Map<String, Object> user = createUserMissingUsername();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(user)
        .when()
            .post("/user")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="user-NumericEmail")
    public void testNumericEmail_ShouldFail() {
        String username = generateRandomUsername();
        Map<String, Object> user = createUserWithNumericEmail(username);

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(user)
        .when()
            .post("/user")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="user-emptyUser")
    public void testEmptyUser_ShouldFail() {
        Map<String, Object> user = createEmptyUser();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(user)
        .when()
            .post("/user")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }
}
