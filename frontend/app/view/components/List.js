export default function List({
                                       recipes,
                                       onDelete,
                                   }) {
    // шf there are no recipes, display an empty message
    if (recipes.length === 0) {
        return (
            <div className="card empty">
                No recipes yet. Add your first recipe!
            </div>
        );
    }

    // display all recipes in a grid
    return (
        <section className="recipes-grid">
            {/* Go through every recipe and create a card for it */}
            {recipes.map((recipe) => (
                <article
                    className="recipe-card"
                    // react uses the key to identify each recipe
                    key={recipe.id}
                >
                    {/* display the recipe name */}
                    <h3>{recipe.name}</h3>

                    {/* display the recipe author */}
                    <p>
                        <span className="label">
                            Author:
                        </span>{" "}
                        {recipe.author}
                    </p>

                    {/* display the recipe description */}
                    <p className="recipe-description">
                        <span className="label">
                            Description:
                        </span>{" "}
                        {recipe.recipeDescription}
                    </p>

                    {/* display recipe creation date */}
                    <p>
                        <span className="label">
                            Created:
                        </span>{" "}
                        {recipe.formattedCreatedAt}
                    </p>

                    <h4>Ingredients</h4>

                    {/* display list of recipe ingredients */}
                    <ul>
                        {recipe.ingredients.map(
                            (ingredient, index) => (
                                <li
                                    // use the ingredient ID as the key, if there is no ID, use the array index.
                                    key={
                                        ingredient.id ??
                                        index
                                    }
                                >
                                    {/* display ingredient name, amount, and unit */}
                                    {ingredient.name}{" "}
                                    —{" "}
                                    {ingredient.amount}{" "}
                                    {ingredient.unit}
                                </li>
                            )
                        )}
                    </ul>

                    {/* delete the selected recipe */}
                    <button
                        type="button"
                        className="delete-button"
                        onClick={() =>
                            onDelete(recipe.id)
                        }
                    >
                        Delete
                    </button>
                </article>
            ))}
        </section>
    );
}