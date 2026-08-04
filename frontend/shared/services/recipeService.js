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
    const result = await fetchAllRecipes();

    if (!result.success) {
        return result;
    }

    return {
        success: true,
        data: transformRecipes(result.data),
    };
}

// create a new recipe
export async function addNewRecipe(recipeDto) {
    const result = await postRecipe(recipeDto);

    if (!result.success) {
        return result;
    }

    return {
        success: true,
        data: transformRecipe(result.data),
    };
}

export async function deleteRecipe(id) {
    return removeRecipe(id);
}