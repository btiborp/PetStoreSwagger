package com.example.sandbox.user;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.report.TestListener;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static com.example.sandbox.util.factory.UserTestDataFactory.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.put;

import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;

/*
 *  Elérhető fő végpontok a Swagger alapján:
    Művelet	Végpont	                Leírás
    POST	/user	                új felhasználó létrehozása
    GET	    /user/{username}	    felhasználó lekérése
    PUT	    /user/{username}	    felhasználó módosítása
    DELETE	/user/{username}	    törlés
    GET	    /user/login	            bejelentkezés név/jelszóval
    GET	    /user/logout	        kijelentkezés
    POST	/user/createWithArray	tömeges felhasználó létrehozás
    POST	/user/createWithList	tömeges felhasználó létrehozás

    pozitív felhasználói end-to-end tesztek
    Teszteljük:
    1. új felhasználó létrehozása (POST /user)
    2. lekérdezése (GET /user/{username})
    3. módosítása (PUT /user/{username})
    4. törlése (DELETE /user/{username})
    5. újra lekérés → 404

    Mit ellenőriz ez?
    Lépés	Végpont	            Ellenőrzés
    1	    POST /user	        200 + helyes ID válasz
    2	    GET /user/{name}	felhasználó elérhető
    3	    PUT /user/{name}	módosítás sikeres
    4	    DELETE /user/{name}	törlés sikeres
    5	    GET újra	        404 – nincs többé
*/
@Listeners(TestListener.class)
public class UserLifecycleTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @Test(enabled = true,groups = {SMOKE},description ="UserLifecycle-scenario")
    public void testUserLifecycle() {
        String username = generateRandomUsername();
        Map<String, Object> newUser = createValidUser(username);

        // 1️⃣ POST /user
        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(newUser)
        .when()
            .post("/user")
        .then()
            .statusCode(200)
            .body("message", equalTo(String.valueOf(newUser.get("id"))));

        // 2️⃣ GET /user/{username}
        Response getResp = given()
            .baseUri(BASE_URI)
            .pathParam("username", username)
        .when()
            .get("/user/{username}");

        Assert.assertEquals(getResp.statusCode(), 200);
        Assert.assertEquals(getResp.jsonPath().getString("username"), username);

        // 3️⃣ PUT /user/{username} (update)
        Map<String, Object> updatedUser = createUpdatedUser(username);

        given()
            .baseUri(BASE_URI)
            .pathParam("username", username)
            .contentType(ContentType.JSON)
            .body(updatedUser)
        .when()
            .put("/user/{username}")
        .then()
            .statusCode(200);

        // 4️⃣ DELETE /user/{username}
        given()
            .baseUri(BASE_URI)
            .pathParam("username", username)
        .when()
            .delete("/user/{username}")
        .then()
            .statusCode(200);

        // 5️⃣ GET again → expect 404
        given()
            .baseUri(BASE_URI)
            .pathParam("username", username)
        .when()
            .get("/user/{username}")
        .then()
            .statusCode(404);
    }

}
