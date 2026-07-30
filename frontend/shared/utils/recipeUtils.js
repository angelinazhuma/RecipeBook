export function createRecipeDto(
    name,
    author,
    recipeDescription,
    ingredients
) {
    return {
        name,
        author,
        recipeDescription,
        ingredients,
    };
}