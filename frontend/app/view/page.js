"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import {
    deleteRecipe,
    getAllRecipes,
} from "../../shared/services/recipeService";

import {
    getToken,
    logoutUser,
} from "../../shared/services/authService";

import List from "./components/list";

export default function ViewRecipesPage() {
    const router = useRouter();

    const [recipes, setRecipes] =
        useState([]);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState("");

    useEffect(() => {
        // Redirects to login if there is no token
        if (!getToken()) {
            router.replace("/login");
            return;
        }

        let cancelled = false;

        // Loads recipes from the backend
        getAllRecipes()
            .then((data) => {
                if (!cancelled) {
                    setRecipes(data);
                }
            })
            .catch((error) => {
                if (cancelled) {
                    return;
                }

                // Removes an invalid or expired token
                if (
                    error.message.includes(
                        "login"
                    )
                ) {
                    logoutUser();
                    router.replace("/login");
                    return;
                }

                setError(error.message);
            })
            .finally(() => {
                if (!cancelled) {
                    setIsLoading(false);
                }
            });

        // Prevents state updates after leaving the page
        return () => {
            cancelled = true;
        };
    }, [router]);

    // Deletes a selected recipe
    const handleDelete = async (id) => {
        setError("");

        try {
            await deleteRecipe(id);

            // Removes the deleted recipe from the page
            setRecipes((currentRecipes) =>
                currentRecipes.filter(
                    (recipe) =>
                        recipe.id !== id
                )
            );
        } catch (error) {
            if (
                error.message.includes(
                    "login"
                )
            ) {
                logoutUser();
                router.replace("/login");
                return;
            }

            setError(error.message);
        }
    };

    if (isLoading) {
        return (
            <section className="card">
                <p>Loading recipes...</p>
            </section>
        );
    }

    return (
        <>
            {error && (
                <section className="card">
                    <p role="alert">
                        {error}
                    </p>
                </section>
            )}

            <List
                recipes={recipes}
                onDelete={handleDelete}
            />
        </>
    );
}