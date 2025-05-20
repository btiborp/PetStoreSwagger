package com.example.sandbox.util.body.pet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetBody {
    private Long id;
    private Item category;
    private String name;
    private List<String> photoUrls;
    private List<Item> tags;
    private String status; // expected values: available, pending, sold
}
