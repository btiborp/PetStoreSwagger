package com.example.sandbox.user;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static com.example.sandbox.util.factory.UserTestDataFactory.*;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import static com.example.sandbox.util.constans.Tags.SMOKE;

/*
 *  Cél: be- és kijelentkezés tesztelése
    Teszt	                        Végpont	Leírás
    1️⃣ Helyes login                 GET /user/login	Jó felhasználónév + jelszó
    2️⃣ Hibás login (rossz jelszó)	 GET /user/login	Hibás adat esetén helyes hibakezelés
    3️⃣ Üres login adatokkal	     GET /user/login	Nincs megadva user/pass
    4️⃣ Logout ellenőrzés	         GET /user/logout	Sikeres kijelentkezés után státusz ellenőrzés

    A Swagger Petstore nem valós autentikációs rendszer, ezért néha 200-at ad hibás loginra is — ilyen eseteket érdemes dokumentálni a BUG-REPORT.md-ben.

    A login és logout nem használ session-t/token-t, csak dummy válaszokat küld vissza.
 * 
*/
public class UserLoginTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";
    private String username = generateRandomUsername();
    private String password = "test123";

    @BeforeClass
    public void createUserForLogin() {
        Map<String, Object> user = createValidUser(username);
        user.put("password", password);

        given()
            .baseUri(BASE_URI)
            .contentType("application/json")
            .body(user)
        .when()
            .post("/user")
        .then()
            .statusCode(200);
    }

    @Test(enabled = true,groups = {SMOKE},description ="userLogin")
    public void testValidLogin() {
        given()
            .baseUri(BASE_URI)
            .queryParam("username", username)
            .queryParam("password", password)
        .when()
            .get("/user/login")
        .then()
            .statusCode(200)
            .body("message", containsString("logged in user session"));
    }

    @Test(enabled = true,groups = {SMOKE},description ="userLogin-WrongPassword")
    public void testInvalidLogin_WrongPassword() {
        given()
            .baseUri(BASE_URI)
            .queryParam("username", username)
            .queryParam("password", "wrongpass")
        .when()
            .get("/user/login")
        .then()
            .statusCode(400); // vagy 200 + hibás üzenet (ha Swagger hibásan viselkedik)
    }

    @Test(enabled = true,groups = {SMOKE},description ="userLogin-EmptyCredentials")
    public void testLoginWithEmptyCredentials() {
        given()
            .baseUri(BASE_URI)
            .queryParam("username", "")
            .queryParam("password", "")
        .when()
            .get("/user/login")
        .then()
            .statusCode(400);
    }

    @Test(enabled = true,groups = {SMOKE},description ="userLogout")
    public void testLogout() {
        given()
            .baseUri(BASE_URI)
        .when()
            .get("/user/logout")
        .then()
            .statusCode(200)
            .body("message", equalTo("ok"));
    }
}
