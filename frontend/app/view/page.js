"use client";

import { useEffect, useState } from "react";

import {
    deleteRecipe,
    getAllRecipes,
} from "../../shared/services/recipeService";

import List from "./components/list";

const ViewRecipesPage = () => {
    const [recipes, setRecipes] = useState([]);

    const loadRecipes = async () => {
        const data = await getAllRecipes();
        setRecipes(data);

    };

    useEffect(() => {
        loadRecipes();
    }, []);

    const handleDelete = async (id) => {


            await deleteRecipe(id);

            setRecipes((currentRecipes) =>
                currentRecipes.filter(
                    (recipe) => recipe.id !== id
                )
            );

    };


    return (

            <List
                recipes={recipes}
                onDelete={handleDelete}
        />
    );
}

export default ViewRecipesPage;