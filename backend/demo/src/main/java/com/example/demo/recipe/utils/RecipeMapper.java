package com.example.demo.recipe.utils;

import com.example.demo.recipe.DTO.IngredientDTO;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import com.example.demo.recipe.model.Ingredient;
import com.example.demo.recipe.model.Recipe;

public class RecipeMapper {
  // RecipeRequestDTO to Recipe
  public static Recipe toEntity(RecipeRequestDTO dto) {
    return Recipe.builder()
        .name(dto.getName())
        .author(dto.getAuthor())
        .recipeDescription(dto.getRecipeDescription())
        .ingredients(
            dto.getIngredients().stream().map(ingredientDTO ->
                Ingredient.builder()
                    .unit(ingredientDTO.getUnit())
                    .amount(ingredientDTO.getAmount())
                    .name(ingredientDTO.getName())
                    .build()
            ).toList()
        ).build();
  }

  // Recipe to RecipeResponseDTO
  public static RecipeResponseDTO toResponseDTO(
      Recipe recipe
  ) {
    return RecipeResponseDTO.builder()
        .id(recipe.getId())
        .name(recipe.getName())
        .author(recipe.getAuthor())
        .recipeDescription(
            recipe.getRecipeDescription()
        )
        .createdAt(recipe.getCreatedAt())
        .ingredients(
            recipe.getIngredients()
                .stream()
                .map(ingredient ->
                    IngredientDTO.builder()
                        .name(ingredient.getName())
                        .amount(ingredient.getAmount())
                        .unit(ingredient.getUnit())
                        .build()
                ).toList()
        ).build();
  }
}