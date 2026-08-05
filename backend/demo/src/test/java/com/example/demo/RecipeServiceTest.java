package com.example.demo;

import com.example.demo.recipe.model.Ingredient;
import com.example.demo.recipe.model.Recipe;
import com.example.demo.recipe.repository.Repository;
import com.example.demo.recipe.service.Service;
import com.example.demo.recipe.DTO.IngredientDTO;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    //fake repository created by mockito
    @Mock
    private Repository repository;


    @InjectMocks
    private Service service;

    private Recipe createRecipe(
            Long id,
            String name,
            String author,
            String description
    ) {
        Recipe recipe = new Recipe();

        recipe.setId(id);
        recipe.setName(name);
        recipe.setAuthor(author);
        recipe.setRecipeDescription(description);
        recipe.setCreatedAt(
                LocalDateTime.of(
                        2026,
                        7,
                        29,
                        12,
                        0
                )
        );

        Ingredient ingredient = new Ingredient();

        ingredient.setId(10L);
        ingredient.setName("Salt");
        ingredient.setAmount(5.0);
        ingredient.setUnit("g");

        //ingredient must know which recipe it belongs to

        ingredient.setRecipe(recipe);

        recipe.setIngredients(List.of(ingredient));

        return recipe;
    }

    private RecipeRequestDTO createRequestDTO(
            String name,
            String author,
            String description
    ) {
        IngredientDTO ingredientDTO = new IngredientDTO();

        ingredientDTO.setName("Salt");
        ingredientDTO.setAmount(5.0);
        ingredientDTO.setUnit("g");

        RecipeRequestDTO requestDTO = new RecipeRequestDTO();

        requestDTO.setName(name);
        requestDTO.setAuthor(author);
        requestDTO.setRecipeDescription(description);
        requestDTO.setIngredients(List.of(ingredientDTO));

        return requestDTO;
    }

}