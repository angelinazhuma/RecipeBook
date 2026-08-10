package com.example.demo.recipe.service;

import com.example.demo.authorization.repository.UserRepository;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import com.example.demo.recipe.model.Recipe;
import com.example.demo.authorization.model.User;
import com.example.demo.recipe.repository.RecipeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.recipe.utils.RecipeMapper;
import java.util.List;

import static com.example.demo.recipe.utils.RecipeMapper.toEntity;
import static com.example.demo.recipe.utils.RecipeMapper.toResponseDTO;

@org.springframework.stereotype.Service
public class RecipeService {

  @Autowired
  private RecipeRepository repository;
  @Autowired
  private UserRepository userRepository;


  // get all recipes of current user
  public List<RecipeResponseDTO> getAllRecipes(
      Long userId
  ) {
    return repository
        .findAllByUserId(userId)
        .stream()
        .map(RecipeMapper::toResponseDTO)
        .toList();
  }

  // get one recipe of current user by id
  public RecipeResponseDTO getRecipeById(
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

    return toResponseDTO(recipe);
  }

  // save new recipe for current user
  public RecipeResponseDTO saveRecipe(
      RecipeRequestDTO dto,
      Long userId
  ) {
    Recipe recipe = toEntity(dto);

    User user =
        userRepository.getReferenceById(userId);

    recipe.setUser(user);

    recipe.getIngredients().forEach(
        ingredient ->
            ingredient.setRecipe(recipe)
    );

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

}