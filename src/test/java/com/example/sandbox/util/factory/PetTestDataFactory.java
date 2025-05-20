package com.example.sandbox.util.factory;

import com.example.sandbox.util.body.pet.Item;
import com.example.sandbox.util.body.pet.PetBody;
import com.example.sandbox.util.body.pet.PostCreatePet;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PetTestDataFactory {

    public static PostCreatePet createValidPetRequest() {
        return PostCreatePet.builder()
            .petBody(generatePetBody())
            .build();
    }

    public static PetBody generatePetBody() {
        long id = generateUniqueId();
        return PetBody.builder()
            .id(id)
            .name("TestPet-" + id)
            .category(Item.builder()
                .id(1L)
                .name("TestCategory")
                .build())
            .photoUrls(List.of("https://example.com/image.jpg"))
            .tags(List.of(
                Item.builder().id(100L).name("tag1").build(),
                Item.builder().id(101L).name("tag2").build()))
            .status("available")
            .build();
    }

    public static long generateUniqueId() {
        return System.currentTimeMillis() + ThreadLocalRandom.current().nextInt(100, 999);
    }

    // Hiányzik a name mező
    public static PostCreatePet createPetMissingName() {
    PetBody pet = PetBody.builder()
        .id(generateUniqueId())
        .category(Item.builder().id(1L).name("Test").build())
        .photoUrls(List.of("https://img"))
        .status("available")
        .build();

    return PostCreatePet.builder()
        .petBody(pet)
        .build();
    }

    // Üres photoUrls
    public static PostCreatePet createPetWithEmptyPhotoUrls() {
    PetBody pet = PetBody.builder()
        .id(generateUniqueId())
        .name("Nameless")
        .photoUrls(List.of()) // ❌ üres lista
        .status("available")
        .build();

    return PostCreatePet.builder()
        .petBody(pet)
        .build();
    }

    // Hibás státusz (nem enum tag)
    public static PostCreatePet createPetWithInvalidStatus() {
    PetBody pet = PetBody.builder()
        .id(generateUniqueId())
        .name("BadStatus")
        .photoUrls(List.of("https://img"))
        .status("ghost") // ❌ nem valid: available, pending, sold
        .build();

    return PostCreatePet.builder()
        .petBody(pet)
        .build();
    }

}
