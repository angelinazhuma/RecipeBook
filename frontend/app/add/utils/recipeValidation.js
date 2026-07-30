export function validateRecipe(
    name,
    author,
    recipeDescription,
    ingredientFields
) {
    if (!name.trim()) {
        return "Enter recipe name";
    }

    if (!author.trim()) {
        return "Enter author name";
    }

    if (!recipeDescription.trim()) {
        return "Enter recipe description";
    }

    for (const ingredient of ingredientFields) {
        if (
            !ingredient.name.trim() ||
            ingredient.amount === "" ||
            !ingredient.unit.trim()
        ) {
            return "Fill in all ingredient fields";
        }

    }

    return null;
}