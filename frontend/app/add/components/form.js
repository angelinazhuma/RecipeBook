"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import { addNewRecipe } from "@/shared/services/recipeService";
import { logoutUser } from "@/shared/services/authService";
import { createRecipeDto } from "../utils/createRecipeDto";
import { validateRecipe } from "../utils/recipeValidation";
import {
    addEmptyIngredient,
    removeIngredientByIndex,
    updateIngredientField,
} from "../utils/ingredientUtils";
import {
    MAX_INGREDIENTS,
} from "@/shared/constants/recipeConstants";

export default function Form() {
    const router = useRouter();

    const [name, setName] = useState("");
    const [author, setAuthor] = useState("");
    const [recipeDescription, setRecipeDescription] =
        useState("");

    const [ingredientFields, setIngredientFields] =
        useState([
            {
                name: "",
                amount: "",
                unit: "",
            },
        ]);


    const changeIngredient = (
        index,
        field,
        value
    ) => {
        setIngredientFields(
            (currentIngredients) =>
                updateIngredientField(
                    currentIngredients,
                    index,
                    field,
                    value
                )
        );
    };

    const handleAddIngredient = () => {
        setIngredientFields(
            (currentIngredients) =>
                addEmptyIngredient(
                    currentIngredients
                )
        );
    };

    const handleRemoveIngredient = (index) => {
        setIngredientFields(
            (currentIngredients) =>
                removeIngredientByIndex(
                    currentIngredients,
                    index
                )
        );
    };

    const addRecipe = async (event) => {
        event.preventDefault();

        const validationError = validateRecipe(
            name,
            author,
            recipeDescription,
            ingredientFields
        );

        if (validationError) {
            alert(validationError);
            return;
        }

        const recipeDto = createRecipeDto(
            name,
            author,
            recipeDescription,
            ingredientFields
        );

        try {
            await addNewRecipe(recipeDto);

            setName("");
            setAuthor("");
            setRecipeDescription("");
            setIngredientFields([
                {
                    name: "",
                    amount: "",
                    unit: "",
                },
            ]);

            router.push("/view");
        } catch (error) {
            // Redirects to login if the token is invalid
            if (error.message.includes("login")) {
                logoutUser();
                router.replace("/login");
                return;
            }

            alert(error.message);
        }
    };

    return (
        <section className="card">
            <h2>Add new recipe</h2>

            <form
                className="form"
                onSubmit={addRecipe}
            >
                <input
                    type="text"
                    placeholder="Recipe name"
                    value={name}
                    onChange={(event) =>
                        setName(
                            event.target.value
                        )
                    }
                />

                <input
                    type="text"
                    placeholder="Author"
                    value={author}
                    onChange={(event) =>
                        setAuthor(
                            event.target.value
                        )
                    }
                />

                <textarea
                    placeholder="Recipe Description"
                    value={recipeDescription}
                    rows={4}
                    onChange={(event) => {
                        setRecipeDescription(
                            event.target.value
                        );

                        event.target.style.height =
                            "auto";

                        event.target.style.height =
                            `${event.target.scrollHeight}px`;
                    }}
                />

                <h3>Ingredients</h3>

                {ingredientFields.map(
                    (ingredient, index) => (
                        <div
                            className="ingredient-row"
                            key={index}
                        >
                            <input
                                type="text"
                                placeholder="Ingredient name"
                                value={
                                    ingredient.name
                                }
                                onChange={(
                                    event
                                ) =>
                                    changeIngredient(
                                        index,
                                        "name",
                                        event.target
                                            .value
                                    )
                                }
                            />

                            <input
                                type="number"
                                placeholder="Amount"
                                min="0.01"
                                step="0.01"
                                value={
                                    ingredient.amount
                                }
                                onChange={(
                                    event
                                ) =>
                                    changeIngredient(
                                        index,
                                        "amount",
                                        event.target
                                            .value
                                    )
                                }
                            />

                            <input
                                type="text"
                                placeholder="Unit"
                                value={
                                    ingredient.unit
                                }
                                onChange={(
                                    event
                                ) =>
                                    changeIngredient(
                                        index,
                                        "unit",
                                        event.target
                                            .value
                                    )
                                }
                            />

                            <button
                                type="button"
                                className="delete-button"
                                onClick={() =>
                                    handleRemoveIngredient(
                                        index
                                    )
                                }
                            >
                                X
                            </button>
                        </div>
                    )
                )}

                {ingredientFields.length <
                    MAX_INGREDIENTS && (
                        <button
                            type="button"
                            className="secondary-button"
                            onClick={
                                handleAddIngredient
                            }
                        >
                            + Add ingredient
                        </button>
                    )}

                <button
                    type="submit"
                    className="add-button"
                >
                    Add Recipe
                </button>
            </form>
        </section>
    );
}
