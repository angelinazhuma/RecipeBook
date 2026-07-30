export function updateIngredientField(
    ingredients,
    index,
    field,
    value
) {
    return ingredients.map((ingredient, currentIndex) => {
        if (currentIndex !== index) {
            return ingredient;
        }

        return {
            ...ingredient,
            [field]: value,
        };
    });
}

export function createEmptyIngredient() {
    return {
        name: "",
        amount: "",
        unit: "",
    };
}

export function addEmptyIngredient(ingredients) {
    return [
        ...ingredients,
        createEmptyIngredient(),
    ];
}

export function removeIngredientByIndex(
    ingredients,
    indexToRemove
) {
    return ingredients.filter(
        (_, currentIndex) =>
            currentIndex !== indexToRemove
    );
}