package com.example.sandbox.user;

import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.example.sandbox.util.factory.UserTestDataFactory.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;


/*
 * Negatív forgatókönyvek
Eset
1️⃣ Hiányzó kötelező mező a lista egyik elemében	
2️⃣ Hibás típus (pl. email = szám) egy felhasználónál	
3️⃣ Üres lista	
4️⃣ Nem lista típusú body
*/
public class UserBulkCreateNegativeTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="userList-MissingField")
    public void testCreateWithMissingField_ShouldFail() {
        List<Map<String, Object>> users = createUserListWithMissingField();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(users)
        .when()
            .post("/user/createWithArray")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="userList-WrongType")
    public void testCreateWithInvalidType_ShouldFail() {
        List<Map<String, Object>> users = createUserListWithWrongType();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(users)
        .when()
            .post("/user/createWithList")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="user-emptyList")
    public void testCreateWithEmptyList_ShouldFail() {
        List<Map<String, Object>> emptyList = Collections.emptyList();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(emptyList)
        .when()
            .post("/user/createWithArray")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="userList-noList")
    public void testCreateWithNonArrayBody_ShouldFail() {
        Map<String, Object> notAList = Map.of("invalid", "value");

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(notAList)
        .when()
            .post("/user/createWithList")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }
}
