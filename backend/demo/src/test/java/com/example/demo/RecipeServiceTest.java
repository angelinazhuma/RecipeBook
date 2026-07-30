package com.example.demo;

import com.example.demo.recipe.model.Ingredient;
import com.example.demo.recipe.model.Recipe;
import com.example.demo.recipe.repository.Repository;
import com.example.demo.recipe.service.Service;
import com.example.demo.recipe.DTO.IngredientDTO;
import com.example.demo.recipe.DTO.RecipeRequestDTO;
import com.example.demo.recipe.DTO.RecipeResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    //fake repository created by mockito
    @Mock
    private Repository repository;


    @InjectMocks
    private Service service;

 //GET all recipes


    @Test
    void getAllRecipesReturnsAllRecipesFromRepository() {
        // Arrange
        Recipe firstRecipe = createRecipe(
                15L,
                "Pasta",
                "Angelina",
                "Italian pasta"
        );

        Recipe secondRecipe = createRecipe(
                84L,
                "Random soup",
                "Random author",
                "Random description"
        );

        Mockito.when(repository.findAll())
                .thenReturn(List.of(firstRecipe, secondRecipe));

        List<RecipeResponseDTO> result = service.getAllRecipes();

        Assertions.assertEquals(2, result.size());

        RecipeResponseDTO firstResult = result.get(0);

        Assertions.assertEquals(15L, firstResult.getId());
        Assertions.assertEquals("Pasta", firstResult.getName());
        Assertions.assertEquals("Angelina", firstResult.getAuthor());
        Assertions.assertEquals(
                "Italian pasta",
                firstResult.getRecipeDescription()
        );

        Assertions.assertEquals(1, firstResult.getIngredients().size());
        Assertions.assertEquals(
                "Salt",
                firstResult.getIngredients().get(0).getName()
        );
        Assertions.assertEquals(
                5.0,
                firstResult.getIngredients().get(0).getAmount()
        );
        Assertions.assertEquals(
                "g",
                firstResult.getIngredients().get(0).getUnit()
        );

        RecipeResponseDTO secondResult = result.get(1);

        Assertions.assertEquals(84L, secondResult.getId());
        Assertions.assertEquals("Random soup", secondResult.getName());
        Assertions.assertEquals(
                "Random author",
                secondResult.getAuthor()
        );

        Mockito.verify(repository).findAll();
    }

   //Get recipebyid

    @Test
    void getRecipeByIdReturnsEmptyOptionalWhenRandomIdDoesNotExist() {
        Long randomId = 937L;

        Mockito.when(repository.findById(randomId))
                .thenReturn(Optional.empty());

        Optional<RecipeResponseDTO> result =
                service.getRecipeById(randomId);

        Assertions.assertTrue(result.isEmpty());

        Mockito.verify(repository).findById(randomId);
    }
    @Test
    void getRecipeByIdReturnsCorrectRecipeWhenRecipeExists() {
        Long expectedId = 25L;

        Recipe recipe = createRecipe(
                expectedId,
                "Pizza",
                "Bob",
                "Pizza description"
        );

        Mockito.when(repository.findById(expectedId))
                .thenReturn(Optional.of(recipe));

        Optional<RecipeResponseDTO> result =
                service.getRecipeById(expectedId);

        Assertions.assertTrue(result.isPresent());

        RecipeResponseDTO responseDTO = result.get();

        Assertions.assertEquals(expectedId, responseDTO.getId());
        Assertions.assertEquals("Pizza", responseDTO.getName());
        Assertions.assertEquals("Bob", responseDTO.getAuthor());
        Assertions.assertEquals(
                "Pizza description",
                responseDTO.getRecipeDescription()
        );
        Assertions.assertEquals(
                "Salt",
                responseDTO.getIngredients().get(0).getName()
        );

        Mockito.verify(repository).findById(expectedId);
    }

    @Test
    void saveRecipeInitiallySendsRecipeWithNullId() {
        // Arrange
        RecipeRequestDTO requestDTO = createRequestDTO(
                "New recipe",
                "Test author",
                "New recipe description"
        );

        Mockito.when(repository.save(Mockito.any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe recipe = invocation.getArgument(0);

                    /*
                     * Before repository.save(), a new recipe has no ID.
                     * The database normally generates it during saving.
                     */
                    Assertions.assertNull(recipe.getId());

                    recipe.setId(100L);
                    return recipe;
                });

        // Act
        RecipeResponseDTO result =
                service.saveRecipe(requestDTO);

        // Assert
        Assertions.assertEquals(100L, result.getId());
        Assertions.assertEquals("New recipe", result.getName());
        Assertions.assertEquals(
                "Test author",
                result.getAuthor()
        );

        Mockito.verify(repository)
                .save(Mockito.any(Recipe.class));
    }

    @Test
    void saveRecipeWorksWithRandomValues() {
        RecipeRequestDTO requestDTO = createRequestDTO(
                "Recipe 847",
                "Author 392",
                "Description 581"
        );

        Mockito.when(repository.save(Mockito.any(Recipe.class)))
                .thenAnswer(invocation -> {
                    Recipe recipe = invocation.getArgument(0);
                    recipe.setId(847L);
                    return recipe;
                });

        RecipeResponseDTO result =
                service.saveRecipe(requestDTO);

        Assertions.assertEquals(847L, result.getId());
        Assertions.assertEquals("Recipe 847", result.getName());
        Assertions.assertEquals("Author 392", result.getAuthor());
        Assertions.assertEquals(
                "Description 581",
                result.getRecipeDescription()
        );

        Mockito.verify(repository)
                .save(Mockito.argThat(recipe ->
                        recipe.getName().equals("Recipe 847")
                                && recipe.getAuthor().equals("Author 392")
                                && recipe.getRecipeDescription()
                                .equals("Description 581")
                ));
    }
    @Test
    void deleteRecipeSendsZeroIdToRepository() {

        service.deleteRecipe(0L);
        Mockito.verify(repository).deleteById(0L);
    }

    @Test
    void deleteRecipeSendsRandomIdToRepository() {

        Long randomId = 739L;

        service.deleteRecipe(randomId);

        Mockito.verify(repository).deleteById(randomId);
    }

    @Test
    void deleteRecipeSendsExpectedIdToRepositoryExactlyOnce() {

        Long expectedId = 25L;

        service.deleteRecipe(expectedId);

        Mockito.verify(repository, Mockito.times(1))
                .deleteById(expectedId);

        Mockito.verifyNoMoreInteractions(repository);
    }



    //helful methods

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