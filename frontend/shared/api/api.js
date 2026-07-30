
import {
    getToken,
} from "../services/authService";

const API_URL =
    process.env.NEXT_PUBLIC_API_URL || "http://172.17.222.129:8080";

// GET /recipes
export async function fetchAllRecipes() {
    // Gets the saved JWT token
    const token = getToken();

    if (!token) {
        throw new Error("Please login first");
    }

    const response = await fetch(
        `${API_URL}/recipes`,
        {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    if (!response.ok) {
        if (response.status === 401) {
            throw new Error(
                "Your login has expired. Please login again."
            );
        }

        throw new Error(
            "Failed to load recipes"
        );
    }

    return response.json();
}

// POST /recipes
export async function postRecipe(recipe) {
    // Gets the saved JWT token
    const token = getToken();

    if (!token) {
        throw new Error("Please login first");
    }

    const response = await fetch(
        `${API_URL}/recipes`,
        {
            method: "POST",

            headers: {
                "Content-Type": "application/json",
                Authorization: `Bearer ${token}`,
            },

            body: JSON.stringify(recipe),
        }
    );

    if (!response.ok) {
        if (response.status === 401) {
            throw new Error(
                "Your login has expired. Please login again."
            );
        }

        const errorText =
            await response.text();

        throw new Error(
            errorText ||
            "Failed to create recipe"
        );
    }

    return response.json();
}

// DELETE /recipes/{id}
export async function removeRecipe(id) {
    // Gets the saved JWT token
    const token = getToken();

    if (!token) {
        throw new Error("Please login first");
    }

    const response = await fetch(
        `${API_URL}/recipes/${id}`,
        {
            method: "DELETE",

            headers: {
                Authorization: `Bearer ${token}`,
            },
        }
    );

    if (!response.ok) {
        if (response.status === 401) {
            throw new Error(
                "Your login has expired. Please login again."
            );
        }

        throw new Error(
            "Failed to delete recipe"
        );
    }
}