package com.example.demo.recipe.controller;

import com.example.demo.recipe.service.Service;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired // spring automatically makes recipeService object and places it here
    private Service service;

    // GET all recipes
    @GetMapping
    public List<RecipeResponseDTO> getAllRecipes() {
        return service.getAllRecipes();
    }
    // GET recipe by id
    @GetMapping("/{id}") // получение рецепта по id
    public Optional<RecipeResponseDTO> getRecipeById(@PathVariable Long id) {
        return service.getRecipeById(id);
    }

    // ADD new recipe
    @PostMapping
    public RecipeResponseDTO saveRecipe(
            @Valid @RequestBody RecipeRequestDTO recipeDTO
    ) {
        return service.saveRecipe(recipeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteRecipe(@PathVariable Long id) {
        service.deleteRecipe(id);
    }

}