import {
    fetchAllRecipes,
    postRecipe,
    removeRecipe
} from "../api/api";

import {
    transformRecipe,
    transformRecipes
} from "../utils/recipeTransformer";

// get all recipes from the backend
export async function getAllRecipes() {

    // send a GET request and wait for the response
    const recipes = await fetchAllRecipes();

    // transform the received data before returning it
    return transformRecipes(recipes);
}

// create a new recipe
export async function addNewRecipe(recipeDto) {

    // send a POST request with the recipe data
    const createdRecipe = await postRecipe(recipeDto);

    // transform the created recipe before returning it
    return transformRecipe(createdRecipe);
}

// delete a recipe by its ID
export async function deleteRecipe(id) {

    // send a DELETE request to the backend
    await removeRecipe(id);
}