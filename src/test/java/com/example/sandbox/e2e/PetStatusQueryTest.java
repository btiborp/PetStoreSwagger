package com.example.sandbox.getPet;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import utils.report.TestListener;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import static com.example.sandbox.util.constans.Tags.SMOKE;

/*
Funkcióteszt: GET /pet/findByStatus
Cél
- Lekérdezni a háziállatokat státusz szerint (available / pending / sold)
- Ellenőrizni, hogy minden visszakapott pet valóban az adott státuszban van
- Vizsgálni, hogy válaszidő gyors (pl. < 1000 ms)
Mit ellenőriz ez?
- A válasz HTTP státuszkódja 200
- A válaszidő < 1500 ms
- A JSON tömb nem üres
- Minden pet status mezője megegyezik a lekérdezett státusszal
 */
@Listeners(TestListener.class)
public class PetStatusQueryTest {

    private static final String BASE_URI = "https://petstore.swagger.io/v2";

    @DataProvider(name = "statusProvider")
    public Object[][] statusProvider() {
        return new Object[][]{
            {"available"},
            {"pending"},
            {"sold"}
        };
    }

    @Test(enabled = true,groups = {SMOKE},description ="PetStatusQuery",dataProvider = "statusProvider")
    public void testFindByStatus(String status) {
        Response response = given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .queryParam("status", status)
        .when()
            .get("/pet/findByStatus")
        .then()
            .statusCode(200)
            .time(lessThan(3000L))
            .extract().response();

        List<Map<String, Object>> pets = response.jsonPath().getList("$");
        // System.out.println(">>> pets.size(): " + pets.size());
        Assert.assertTrue(pets.size() > 0, "Üres lista jött vissza a státuszra: " + status);

        for (Map<String, Object> pet : pets) {
            Assert.assertEquals(
                pet.get("status"),
                status,
                "Eltérő státusz a válaszban: " + pet.get("id"));
        }
    }

    /*
    Cél: Lekérdezni nem létező státusszal (pl. status=ghost)
    Ellenőrizni, hogy:
    - vagy 0 elemet ad vissza
    - vagy hibát (pl. 400 Bad Request)
    */
    @Test(enabled = true,groups = {SMOKE},description ="QueryPetInvalidStatus")
    public void testFindByInvalidStatus_ShouldReturnEmptyOrError() {
        String invalidStatus = "ghost";

        Response response = given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .queryParam("status", invalidStatus)
        .when()
            .get("/pet/findByStatus")
        .then()
            .statusCode(anyOf(equalTo(200), equalTo(400))) // Swagger gyakran 200-at ad üres listára
            .extract().response();

        if (response.statusCode() == 200) {
            List<?> pets = response.jsonPath().getList("$");
            Assert.assertTrue(pets.isEmpty(), "Válaszként nem üres listát kaptunk invalid státuszra.");
        }
    }

}
