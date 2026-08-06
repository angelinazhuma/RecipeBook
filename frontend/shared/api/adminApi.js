const API_URL =
    process.env.NEXT_PUBLIC_API_URL
    || "http://localhost:8080";

export async function fetchAllUsers() {
    const response = await fetch(
        `${API_URL}/admin/users`,
        {
            credentials: "include",
        }
    );

    if (!response.ok) {
        throw new Error(
            "Failed to load users"
        );
    }

    return response.json();
}

export async function fetchUserRecipes(userId) {
    const response = await fetch(
        `${API_URL}/admin/users/${userId}/recipes`,
        {
            credentials: "include",
        }
    );

    if (!response.ok) {
        throw new Error(
            "Failed to load user recipes"
        );
    }

    return response.json();
}