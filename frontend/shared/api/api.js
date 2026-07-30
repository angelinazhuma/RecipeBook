
const API_URL =
    process.env.NEXT_PUBLIC_API_URL || "http://172.17.222.129:8080";

// GET /recipes
export async function fetchAllRecipes() {
    const response = await fetch(`${API_URL}/recipes`);

    console.log("GET status:", response.status);

    if (!response.ok) {
        throw new Error("Failed to load recipes");
    }

    return response.json();
}

// POST /recipes
export async function postRecipe(recipe) {
    const response = await fetch(`${API_URL}/recipes`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(recipe),
    });

    console.log("POST status:", response.status);

    if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText);
    }

    return response.json();
}

// DELETE /recipes/{id}
export async function removeRecipe(id) {
    const response = await fetch(`${API_URL}/recipes/${id}`, {
        method: "DELETE",
    });

    if (!response.ok) {
        throw new Error("Failed to delete recipe");
    }
}