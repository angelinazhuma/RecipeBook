package com.example.demo.recipe.controller;

import com.example.demo.authorization.security.AuthenticatedUser;
import com.example.demo.recipe.service.Service;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

// controller gets http-requests and calls service methods


@RestController
@RequestMapping("/recipes")
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://172.17.222.129:3000"
})
public class Controller {

    public final Service service;

  public Controller(Service service) {
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
    public Optional<RecipeResponseDTO> getRecipeById(
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
