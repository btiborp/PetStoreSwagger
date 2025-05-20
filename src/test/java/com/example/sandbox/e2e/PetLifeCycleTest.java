package com.example.sandbox.e2e;

import com.example.sandbox.util.body.pet.PetBody;
import com.example.sandbox.util.factory.PetTestDataFactory;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import com.example.sandbox.util.listener.ExtentTestListener;
import com.example.sandbox.util.listener.TestLogger;
import static com.example.sandbox.util.constans.Tags.SMOKE;

// @Listeners(TestLogger.class)
@Listeners({TestLogger.class, ExtentTestListener.class})
public class PetLifeCycleTest {

    private final String BASE_URI = "https://petstore.swagger.io/v2";
    private Long petId;

    @Test(enabled = true,groups = {SMOKE},description ="PetLifeCycle-scenario")
    public void testPetEndToEndLifecycle() {
        // 1️⃣ Create (POST)
        PetBody newPet = PetTestDataFactory.generatePetBody();
        petId = newPet.getId();

        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(newPet)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .body("id", equalTo(petId.longValue()))
            .body("name", equalTo(newPet.getName()))
            .time(lessThan(1000L));

        // 2️⃣ Update (PUT)
        newPet.setName("UpdatedPetName");
        newPet.setStatus("sold");

        given()
            .baseUri(BASE_URI)
            .contentType(ContentType.JSON)
            .body(newPet)
        .when()
            .put("/pet")
        .then()
            .statusCode(200)
            .body("name", equalTo("UpdatedPetName"))
            .body("status", equalTo("sold"));

        // 3️⃣ Get by ID
        Response getResponse = given()
            .baseUri(BASE_URI)
            .pathParam("petId", petId)
        .when()
            .get("/pet/{petId}");

        Assert.assertEquals(getResponse.statusCode(), 200, "GET failed");
        Assert.assertEquals(getResponse.jsonPath().getString("name"), "UpdatedPetName");
        Assert.assertEquals(getResponse.jsonPath().getString("status"), "sold");

        // 4️⃣ Delete
        given()
            .baseUri(BASE_URI)
            .pathParam("petId", petId)
        .when()
            .delete("/pet/{petId}")
        .then()
            .statusCode(200);

        // 5️⃣ Get after delete → expect 404
        given()
            .baseUri(BASE_URI)
            .pathParam("petId", petId)
        .when()
            .get("/pet/{petId}")
        .then()
            .statusCode(404);
    }
}
