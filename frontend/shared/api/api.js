import {getCsrfToken} from "../utils/csrfToken";

const API_URL =
    process.env.NEXT_PUBLIC_API_URL || "http://172.17.222.129:8080";

// GET /recipes
export async function fetchAllRecipes() {
    const response = await fetch(
        `${API_URL}/recipes`,
        {
            credentials: "include",
        }
    );

    return {
        success: true,
        data: await response.json(),
    };
}

// POST /recipes
export async function postRecipe(recipe) {
    const csrfToken = getCsrfToken();

    const response = await fetch(
        `${API_URL}/recipes`,
        {
            method: "POST",
            credentials: "include",

            headers: {
                "Content-Type": "application/json",
                "X-XSRF-TOKEN": csrfToken,
            },

            body: JSON.stringify(recipe),
        }
    );

    if (response.status === 401) {
        return {
            success: false,
            unauthorized: true,
            error: "Please login first",
        };
    }

    if (response.status === 403) {
        return {
            success: false,
            unauthorized: false,
            error: "CSRF token is missing or invalid",
        };
    }

    if (!response.ok) {
        const errorText =
            await response.text();

        return {
            success: false,
            unauthorized: false,
            error:
                errorText ||
                "Failed to create recipe",
        };
    }

    return {
        success: true,
        data: await response.json(),
    };

}

// DELETE /recipes/{id}
export async function removeRecipe(id) {
    const csrfToken = getCsrfToken();

    const response = await fetch(
        `${API_URL}/recipes/${id}`,
        {
            method: "DELETE",
            credentials: "include",

            headers: {
                "X-XSRF-TOKEN": csrfToken,
            },
        }
    );

    if (!response.ok) {
        return {
            success: false,
            error: "Failed to delete recipe",
        };
    }

    return {
        success: true,
    };
}