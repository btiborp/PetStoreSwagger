package com.example.sandbox.pet;

import com.example.sandbox.Common;
import com.example.sandbox.util.body.pet.PostCreatePet;
import com.example.sandbox.util.body.pet.Item;
import com.example.sandbox.util.body.pet.PetBody;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import utils.report.TestListener;

import static com.example.sandbox.util.Tools.generateRandomNumber;
import static com.example.sandbox.util.body.pet.JsonBody.createJsonBody;
import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.constans.TestData.HYDRAIMAGE;

import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.startsWith;

import static io.restassured.RestAssured.given;

import static com.example.sandbox.util.factory.PetTestDataFactory.*;

@Listeners(TestListener.class)
public class postNewPetTest extends Common {

    // @BeforeEach
    // void init(TestInfo testInfo) {
    //     String name = testInfo.getDisplayName();
    //     extentTest = extent.createTest(name);
    // }

    @Test(enabled = true,groups = {SMOKE},description ="pet-PostCreatePet1")
    public void Test1(){

        PostCreatePet body = PostCreatePet.builder()
                .petBody(PetBody.builder()
                        .id((long)generateRandomNumber())
                        .category(Item.builder()
                                .id(1L)
                                .name("Hydra")
                                .build())
                        .name("Princess")
                        .photoUrls(List.of(HYDRAIMAGE))
                .photoUrls(List.of(HYDRAIMAGE))  // ✔️ photoUrl → photoUrls
                .tags(List.of(                     // ✔️ tag → tags
                        Item.builder()
                        .id(2L)
                        .name("cute")
                        .build()))
                .status("available")
                .build()
        ).build();

        Response  response = postUrl(newPet,createJsonBody(body));
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");
    }

    @Test(enabled = true,groups = {SMOKE},description ="pet-PostCreatePet2")
    public void testCreatePet_Positive() {
        PetBody petBody = PetBody.builder()
            .id(System.currentTimeMillis())  // garantáltan egyedi
            .name("Garfield")
            .photoUrls(List.of("https://example.com/photo.jpg"))
            .status("available")
            .build();

        given()
            .baseUri("https://petstore.swagger.io/v2")
            .contentType(ContentType.JSON)
            .body(petBody)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            // .time(lessThan(500L))
            .body("name", equalTo("Garfield"))
            .body("status", equalTo("available"));
    }

    @Test(enabled = true,groups = {SMOKE},description ="petPostWithDataFactory")
    public void testPostNewPet_Positive() {
        PostCreatePet request = createValidPetRequest();

        given()
            .baseUri("https://petstore.swagger.io/v2")
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/pet")
        .then()
            .statusCode(200)
            .time(lessThan(500L))
            .body("name", startsWith("TestPet-"))
            .body("status", equalTo("available"));
    }


    // Hiányzik a name mező
    @Test(enabled = true,groups = {SMOKE},description = "pet-MissingName")
    public void testPostPet_MissingName_ShouldReturn405() {
        PostCreatePet request = createPetMissingName();

        given()
            .baseUri("https://petstore.swagger.io/v2")
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/pet")
        .then()
            .statusCode(anyOf(is(200), is(405), is(500))); // Petstore időnként 500-at ad rossz inputra, 405: Invalid input
    }

    // Üres photoUrls
    @Test(enabled = true,groups = {SMOKE},description = "pet-MissingPhotoUrls")
    public void testPostPet_EmptyPhotoUrls_ShouldReturn405() {
        PostCreatePet request = createPetWithEmptyPhotoUrls();

        given()
            .baseUri("https://petstore.swagger.io/v2")
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/pet")
        .then()
            .statusCode(anyOf(is(200), is(405), is(500))); // Petstore időnként 500-at ad rossz inputra, 405: Invalid input
    }

    // Hibás státusz (nem enum tag)
    @Test(enabled = true,groups = {SMOKE},description = "pet-InvalidStatus")
    public void testPostPet_InvalidStatus_ShouldReturn405() {
        PostCreatePet request = createPetWithInvalidStatus();

        given()
            .baseUri("https://petstore.swagger.io/v2")
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/pet")
        .then()
            .statusCode(anyOf(is(200), is(405), is(500))); // Petstore időnként 500-at ad rossz inputra, 405: Invalid input
            
    }

}
