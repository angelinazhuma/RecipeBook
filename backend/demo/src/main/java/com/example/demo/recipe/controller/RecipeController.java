package com.example.demo.recipe.controller;

import com.example.demo.authorization.security.AuthenticatedUser;
import com.example.demo.recipe.service.RecipeService;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

// controller gets http-requests and calls service methods


@RestController
@RequestMapping("/recipes")
public class RecipeController {

  public final RecipeService service;

  public RecipeController(RecipeService service) {
    this.service = service;
  }

    // GET all recipes
  @GetMapping
  public List<RecipeResponseDTO> getAllRecipes(
      Authentication authentication
  ) {
      AuthenticatedUser currentUser =
          (AuthenticatedUser) authentication.getPrincipal();

      return service.getAllRecipes(currentUser.id());
  }
    // GET recipe by id
    @GetMapping("/{id}")
    public RecipeResponseDTO getRecipeById(
        @PathVariable Long id,
        Authentication authentication
    ) {
        AuthenticatedUser currentUser =
            (AuthenticatedUser) authentication.getPrincipal();

        return service.getRecipeById(
            id,
            currentUser.id()
        );
    }
    // ADD new recipe
    @PostMapping
    public RecipeResponseDTO saveRecipe(
        @Valid @RequestBody RecipeRequestDTO recipeDTO,
        Authentication authentication
    ) {
        AuthenticatedUser currentUser =
            (AuthenticatedUser) authentication.getPrincipal();

        return service.saveRecipe(
            recipeDTO,
            currentUser.id()
        );
    }


    @DeleteMapping("/{id}")
    public void deleteRecipe(
        @PathVariable Long id,
        Authentication authentication
    ) {
        AuthenticatedUser currentUser =
            (AuthenticatedUser) authentication.getPrincipal();

        service.deleteRecipe(
            id,
            currentUser.id()
        );
    }
}
