package com.example.sandbox.businessProcesses;

import com.example.sandbox.Common;
import com.example.sandbox.util.body.pet.PostCreatePet;
import com.example.sandbox.util.body.pet.Item;
import com.example.sandbox.util.body.pet.PetBody;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import utils.report.TestListener;

import static com.example.sandbox.util.Tools.generateRandomNumber;
import static com.example.sandbox.util.body.pet.JsonBody.createJsonBody;
import static com.example.sandbox.util.constans.Tags.SMOKE;
import static com.example.sandbox.util.constans.TestData.HYDRAIMAGE;

import java.util.List;

@Listeners(TestListener.class)
public class PetLifeCycle extends Common {
    @Test(enabled = true,groups = {SMOKE},description ="PetLifeCycle-post")
    public void Test_PetLifeCycle(){

        // create pet
        PostCreatePet postBody = PostCreatePet.builder()
                .petBody(PetBody.builder()
                .id((long) generateRandomNumber())
                .category(Item.builder()
                        .id(1L)
                        .name("Hydra")
                        .build())
                .name("Princess")
                .photoUrls(List.of(HYDRAIMAGE))
                .tags(List.of(
                        Item.builder()
                        .id(2L)
                        .name("cute")
                        .build()))
                .status("available")
                .build())
        .build();

        Response response = postUrl(newPet, createJsonBody(postBody));
        Assert.assertEquals(response.getStatusCode(),200,"Invalid response code");

        String id = response.jsonPath().get("id").toString();
        System.out.println(">>> post: " + id);

        Response  response2 = getUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response2.getStatusCode(),200,"Invalid response code");     // sokszor 404-es hibaval ter vissza!!!

        // update pet
        PostCreatePet putBody = PostCreatePet.builder()
                .petBody(PetBody.builder()
                .id(Long.parseLong(id))
                .category(Item.builder()
                        .id(1L)
                        .name("Hydra")
                        .build())
                .name("Princess")
                .photoUrls(List.of(HYDRAIMAGE))
                .tags(List.of(
                        Item.builder()
                        .id(2L)
                        .name("cute")
                        .build(),
                        Item.builder()
                        .id(3L)
                        .name("smart")
                        .build()))
                .status("sold")
                .build())
        .build();

        Response  response3 = putUrl(updatePet, createJsonBody(putBody));
        Assert.assertEquals(response3.getStatusCode(),200,"Invalid response code");

        id = response3.jsonPath().get("id").toString();
        System.out.println(">>> put : " + id);

        Response  response4 = getUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response4.getStatusCode(),200,"Invalid response code");

        id = response4.jsonPath().get("id").toString();
        System.out.println(">>> put+: " + id);
        System.out.println(">>> " + response4.jsonPath().get("tags").toString());
        Assert.assertTrue(response4.jsonPath().get("tags").toString().contains("{id=3, name=smart}"), "Update did not contain the new field!");

        id = response4.jsonPath().get("id").toString();
        System.out.println(">>> del : " + id);

        // delete pet
        Response  response5 = deleteUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response5.getStatusCode(),200,"Invalid response code");

        id = response5.jsonPath().get("message").toString();
        System.out.println(">>> del+: " + id);

        Response  response6 = getUrl(petById.replace("{petId}",id));
        Assert.assertEquals(response6.getStatusCode(),404,"Invalid response code");     // sokszor 404-es hibaval ter vissza!!!

    }

}
