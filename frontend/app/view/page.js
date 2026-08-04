"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import {
    deleteRecipe,
    getAllRecipes,
} from "@/shared/services/recipeService";

import {
    getCurrentUser,
} from "@/shared/services/currentUserService";

import {
    logoutUser,
} from "@/shared/services/logoutService";

import List from "./components/List";

export default function ViewRecipesPage() {
    const router = useRouter();

    const [recipes, setRecipes] =
        useState([]);

    const [isLoading, setIsLoading] =
        useState(true);

    const [error, setError] =
        useState("");

    useEffect(() => {
        let cancelled = false;

        // checks authentication through backend
        getCurrentUser()
            .then((authResult) => {
                if (cancelled) {
                    return null;
                }

                if (!authResult.authenticated) {
                    router.replace("/login");
                    return null;
                }

                // loads current user's recipes
                return getAllRecipes();
            })
            .then((recipesResult) => {
                if (
                    cancelled ||
                    recipesResult === null
                ) {
                    return;
                }

                /*
                 * Supports both possible service formats:
                 * 1. plain recipe array
                 * 2. { success, data, error }
                 */
                const loadedRecipes =
                    recipesResult.data ??
                    recipesResult;

                setRecipes(loadedRecipes);
            })
            .catch((requestError) => {
                if (cancelled) {
                    return;
                }

                setError(
                    requestError.message ||
                    "Failed to load recipes"
                );
            })
            .finally(() => {
                if (!cancelled) {
                    setIsLoading(false);
                }
            });

        return () => {
            cancelled = true;
        };
    }, [router]);

    // Deletes a selected recipe
    const handleDelete = (id) => {
        setError("");

        deleteRecipe(id)
            .then((result) => {
                if (
                    result &&
                    result.success === false
                ) {
                    setError(
                        result.error ||
                        "Failed to delete recipe"
                    );
                    return;
                }

                setRecipes(
                    (currentRecipes) =>
                        currentRecipes.filter(
                            (recipe) =>
                                recipe.id !== id
                        )
                );
            })
            .catch((requestError) => {
                const message =
                    requestError.message ||
                    "Failed to delete recipe";

                if (
                    message
                        .toLowerCase() // ignore case
                        .includes("login") || // check for login error
                    message
                        .toLowerCase() //
                        .includes("unauthorized") //
                ) {
                    logoutUser().finally(() => {
                        router.replace("/login");
                    });

                    return;
                }

                setError(message);
            });
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