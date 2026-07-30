// transform a single recipe received from the backend
export function transformRecipe(recipe) {
    return {
        // copy all existing recipe properties
        ...recipe,

        // if the recipe has no ingredients, use an empty array instead of null or undefined
        ingredients: recipe.ingredients || [],

        // format the creation date for display, if the date does not exist, display "No date".
        formattedCreatedAt: recipe.createdAt
            ? new Date(recipe.createdAt).toLocaleString()
            : "No date",
    };
}

// transform a list of recipes
export function transformRecipes(recipes) {

    // apply transformRecipe() to every recipe in the array
    return recipes.map(transformRecipe);
}