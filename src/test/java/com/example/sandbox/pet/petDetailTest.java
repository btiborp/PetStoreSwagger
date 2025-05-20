package com.example.sandbox.pet;

import com.example.sandbox.Common;

import io.restassured.response.Response;

import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import utils.report.TestListener;

import java.util.Map;
import java.util.TreeMap;

import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.helpers.PetApiHelper.getFirstAvailablePetId;

@Listeners(TestListener.class)
public class petDetailTest extends Common {

    @Test(enabled = true,groups = {SMOKE},description ="pet-scenario")
    public void Test_GetAllAvailable(){
        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("status","available");

        Response  response = getUrl(findByStatus, queryParams);
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");

        String id = getFirstAvailablePetId(response);
        Assert.assertNotNull(id, "Nem találtunk elérhető pet-et.");
        Assert.assertNotEquals(id, "null", "Nem találtunk elérhető pet-et.");
        System.out.println(">>> id: " + id);
        Response response2 = getUrl(petById.replace("{petId}", id));
        Assert.assertEquals(response2.getStatusCode(), 200, "Invalid response code");


        // Alternatíva: sima listából ID lekérés
        // List<Map<String, Object>> pets = response.jsonPath().getList("$");
        // String id = pets.get(0).get("id").toString(); // első elérhető pet id-je

        // Response  response2 = getUrl(petById.replace("{petId}",id));
        // System.out.println("Response body: " + response2.getBody().asPrettyString());

        // Assert.assertEquals(response2.getStatusCode(),200,"Invalid response code");
    }

}
