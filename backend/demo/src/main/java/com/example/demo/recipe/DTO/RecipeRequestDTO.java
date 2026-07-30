package com.example.demo.recipe.DTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRequestDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String recipeDescription;

    @Valid
    @NotEmpty
    private List<IngredientDTO> ingredients;
}