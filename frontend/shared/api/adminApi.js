const API_URL =
    process.env.NEXT_PUBLIC_API_URL
    || "http://localhost:8080";

export async function fetchAllUsers(
    page,
    size,
    search
) {
    const response = await fetch(
        `${API_URL}/admin/users?page=${page}&size=${size}&search=${encodeURIComponent(search)}`,
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