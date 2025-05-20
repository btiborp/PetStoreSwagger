package com.example.sandbox.store;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.report.TestListener;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.factory.OrderTestDataFactory.*;

/*
Cél
- POST /store/order → új rendelés létrehozása
- GET /store/order/{orderId} → rendelés lekérése
- DELETE /store/order/{orderId} → törlés
- GET /store/order/{orderId} → ellenőrzés, hogy törlés után 404
 */
@Listeners(TestListener.class)
public class StoreOrderTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="StoreOrder-scenario")
    public void testStoreOrderLifecycle() {
        long orderId = ThreadLocalRandom.current().nextLong(100000, 999999);

        // 1️⃣ POST /store/order
        String orderBody = """
            {
                "id": %d,
                "petId": 1,
                "quantity": 2,
                "shipDate": "2025-06-01T12:00:00.000Z",
                "status": "placed",
                "complete": true
            }
            """.formatted(orderId);

        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(orderBody)
        .when()
            .post("/store/order")
        .then()
            .statusCode(200)
            .body("id", equalTo((int) orderId))
            .body("status", equalTo("placed"));

        // 2️⃣ GET /store/order/{orderId}
        Response response = given()
            .baseUri(BASE_URI)
            .pathParam("orderId", orderId)
        .when()
            .get("/store/order/{orderId}");

        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertEquals(response.jsonPath().getLong("id"), orderId);
        Assert.assertEquals(response.jsonPath().getString("status"), "placed");

        // 3️⃣ DELETE /store/order/{orderId}
        given()
            .baseUri(BASE_URI)
            .pathParam("orderId", orderId)
        .when()
            .delete("/store/order/{orderId}")
        .then()
            .statusCode(200);

        // 4️⃣ GET után 404
        given()
            .baseUri(BASE_URI)
            .pathParam("orderId", orderId)
        .when()
            .get("/store/order/{orderId}")
        .then()
            .statusCode(404);
    }


    /**
     * Ugyanaz mint a felso, csak OrderTestDataFactory -bol veszi az adatokat
       Haszon	                    Miért jó?
       Újrafelhasználható       	más tesztosztályban is
       Könnyen bővíthető	        pl. invalid mezőkkel
       Elkülönített logika	        egyszerűbb, karbantarthatóbb kód
     */
    @Test(enabled = true,groups = {SMOKE},description ="StoreOrder-factory")
    public void testStoreOrderLifecycle2() {
        long orderId = generateUniqueOrderId();
        Map<String, Object> orderBody = createValidOrder(orderId);

        // 1️⃣ Create
        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(orderBody)
        .when()
            .post("/store/order")
        .then()
            .statusCode(200)
            .body("id", equalTo((int) orderId))
            .body("status", equalTo("placed"));

        // ...
    }

    /*
     * 2. /store/inventory – készlet lekérdezés
     * Ez egy egyszerű GET kérés, ami JSON objektumban adja vissza a pet-ek státuszonkénti darabszámát.
     */
    @Test(enabled = true,groups = {SMOKE},description ="queryInventory")
    public void testGetInventory() {
        Response response = given()
            .baseUri(BASE_URI)
        .when()
            .get("/store/inventory");

        Assert.assertEquals(response.statusCode(), 200);
        Map<String, Integer> inventory = response.jsonPath().getMap("$");

        Assert.assertTrue(inventory.containsKey("available"));
        Assert.assertTrue(inventory.containsKey("sold"));
    }

}
