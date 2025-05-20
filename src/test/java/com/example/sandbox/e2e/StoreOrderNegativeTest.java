package com.example.sandbox.store;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import utils.report.TestListener;

import java.util.Map;

import static com.example.sandbox.util.factory.OrderTestDataFactory.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;

/*
    Tesztelendő negatív esetek
    ❌ hiányzik a kötelező mező (pl. petId)
    ❌ negatív mennyiség (quantity = -1)
    ❌ hibás típus (pl. status = 123)
    ❌ teljesen üres rendelés
    A Swagger Petstore nem mindig küld 400-as hibát hibás adatnál, néha csak 500-at vagy 200-at ad vissza üres testtel — ezért az anyOf(400, 500) ellenőrzés.
*/
@Listeners(TestListener.class)
public class StoreOrderNegativeTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="StoreOrder-MissingPetId")
    public void testOrderMissingPetId_ShouldFail() {
        Map<String, Object> order = createOrderMissingPetId(generateUniqueOrderId());

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(order)
        .when()
            .post("/store/order")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="StoreOrder-NegativeQuantity")
    public void testOrderWithNegativeQuantity_ShouldFail() {
        Map<String, Object> order = createOrderWithNegativeQuantity(generateUniqueOrderId());

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(order)
        .when()
            .post("/store/order")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="OrderWithInvalidStatusType")
    public void testOrderWithInvalidStatusType_ShouldFail() {
        Map<String, Object> order = createOrderWithInvalidStatusType(generateUniqueOrderId());

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(order)
        .when()
            .post("/store/order")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }

    @Test(enabled = true,groups = {SMOKE},description ="EmptyOrder")
    public void testEmptyOrder_ShouldFail() {
        Map<String, Object> order = createEmptyOrder();

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(order)
        .when()
            .post("/store/order")
        .then()
            .statusCode(anyOf(equalTo(400), equalTo(500)));
    }
}
