package com.example.demo.recipe.service;

import com.example.demo.recipe.DTO.IngredientDTO;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import com.example.demo.recipe.model.Ingredient;
import com.example.demo.recipe.model.Recipe;
import com.example.demo.authorization.model.User;
import com.example.demo.recipe.repository.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
public class Service {

  @Autowired
  private Repository repository;


  @PersistenceContext
  private EntityManager entityManager;

  // get all recipes of current user
  public List<RecipeResponseDTO> getAllRecipes(
      Long userId
  ) {
    return repository
        .findAllByUserId(userId)
        .stream()
        .map(this::toResponseDTO)
        .toList();
  }

  // get one recipe of current user by id
  public Optional<RecipeResponseDTO> getRecipeById(
      Long recipeId,
      Long userId
  ) {
    return repository
        .findByIdAndUserId(
            recipeId,
            userId
        )
        .map(this::toResponseDTO);
  }

  // save new recipe for current user
  public RecipeResponseDTO saveRecipe(
      RecipeRequestDTO dto,
      Long userId
  ) {
    Recipe recipe = toEntity(dto);

    User userReference =
        entityManager.getReference(
            User.class,
            userId
        );

    recipe.setUser(userReference);

    Recipe savedRecipe =
        repository.save(recipe);

    return toResponseDTO(savedRecipe);
  }

  // delete only current user's recipe
  public void deleteRecipe(
      Long recipeId,
      Long userId
  ) {
    Recipe recipe = repository
        .findByIdAndUserId(
            recipeId,
            userId
        )
        .orElseThrow(() ->
            new RuntimeException(
                "Recipe not found"
            )
        );

    repository.delete(recipe);
  }

  // RecipeRequestDTO to Recipe
  private Recipe toEntity(RecipeRequestDTO dto) {
    Recipe recipe = new Recipe();

    recipe.setName(dto.getName());
    recipe.setAuthor(dto.getAuthor());
    recipe.setRecipeDescription(
        dto.getRecipeDescription()
    );

    List<Ingredient> ingredients = dto.getIngredients()
        .stream()
        .map(ingredientDTO -> {
          Ingredient ingredient = new Ingredient();

          ingredient.setName(
              ingredientDTO.getName()
          );
          ingredient.setAmount(
              ingredientDTO.getAmount()
          );
          ingredient.setUnit(
              ingredientDTO.getUnit()
          );

          ingredient.setRecipe(recipe);

          return ingredient;
        })
        .toList();

    recipe.setIngredients(ingredients);

    return recipe;
  }

  // Recipe to RecipeResponseDTO
  private RecipeResponseDTO toResponseDTO(
      Recipe recipe
  ) {
    RecipeResponseDTO dto =
        new RecipeResponseDTO();

    dto.setId(recipe.getId());
    dto.setName(recipe.getName());
    dto.setAuthor(recipe.getAuthor());
    dto.setRecipeDescription(
        recipe.getRecipeDescription()
    );
    dto.setCreatedAt(recipe.getCreatedAt());

    List<IngredientDTO> ingredients =
        recipe.getIngredients()
            .stream()
            .map(ingredient -> {
              IngredientDTO ingredientDTO =
                  new IngredientDTO();

              ingredientDTO.setName(
                  ingredient.getName()
              );
              ingredientDTO.setAmount(
                  ingredient.getAmount()
              );
              ingredientDTO.setUnit(
                  ingredient.getUnit()
              );

              return ingredientDTO;
            })
            .toList();

    dto.setIngredients(ingredients);

    return dto;
  }
}