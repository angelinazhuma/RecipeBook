export function createRecipeDto(
    name,
    author,
    recipeDescription,
    ingredientFields
) {
    // create and return an object that matches the structure expected by the backend
    return {
        // remove unnecessary spaces from the recipe name
        name: name.trim(),

        // remove unnecessary spaces from the author name
        author: author.trim(),

        // remove unnecessary spaces from the recipe description
        recipeDescription: recipeDescription.trim(),

        // convert every ingredient field into an ingredient DTO
        ingredients: ingredientFields.map(
            (ingredient) => ({
                // remove spaces from the ingredient name
                name: ingredient.name.trim(),

                // convert the amount from a string to a number
                amount: Number(ingredient.amount),

                // remove spaces from the measurement unit
                unit: ingredient.unit.trim(),
            })
        ),
    };
}