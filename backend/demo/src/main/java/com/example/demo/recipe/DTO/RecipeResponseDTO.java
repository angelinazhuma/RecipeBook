package com.example.demo.recipe.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeResponseDTO {

    private Long id;
    private String name;
    private String author;
    private String recipeDescription;
    private LocalDateTime createdAt;
    private List<IngredientDTO> ingredients;

}