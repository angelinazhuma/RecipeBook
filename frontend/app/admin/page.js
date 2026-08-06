"use client";

import { useEffect, useState } from "react";

import {
    fetchAllUsers,
    fetchUserRecipes,
} from "@/shared/api/adminApi";

export default function AdminPage() {
    const [users, setUsers] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [selectedUser, setSelectedUser] =
        useState(null);

    useEffect(() => {
        async function loadUsers() {
            try {
                const data =
                    await fetchAllUsers();

                setUsers(data);
            } catch (error) {
                console.error(error);
            }
        }

        loadUsers();
    }, []);

    async function handleUserClick(user) {
        try {
            const data =
                await fetchUserRecipes(user.id);

            setSelectedUser(user);
            setRecipes(data);
        } catch (error) {
            console.error(error);
        }
    }

    return (
        <main>
            <h1>Admin panel</h1>

            <h2>Users</h2>

            {users.map((user) => (
                <button
                    key={user.id}
                    onClick={() =>
                        handleUserClick(user)
                    }
                >
                    {user.username}
                </button>
            ))}

            {selectedUser && (
                <section>
                    <h2>
                        Recipes of{" "}
                        {selectedUser.username}
                    </h2>

                    <p>
                        Total recipes: {recipes.length}
                    </p>

                    {recipes.length === 0 ? (
                        <p>No recipes</p>
                    ) : (
                        recipes.map((recipe) => (
                            <article
                                key={recipe.id}
                                className="recipe-card"
                            >
                                <h3>{recipe.name}</h3>

                                <hr />

                                <p>
                                    <strong>Author:</strong>{" "}
                                    {recipe.author}
                                </p>

                                <div>
                                    <strong>
                                        Description:
                                    </strong>

                                    <p>
                                        {
                                            recipe
                                                .recipeDescription
                                        }
                                    </p>
                                </div>

                                <p>
                                    <strong>Created:</strong>{" "}
                                    {recipe.createdAt
                                        ? new Date(
                                            recipe.createdAt
                                        ).toLocaleDateString()
                                        : "Unknown"}
                                </p>

                                <div>
                                    <strong>
                                        Ingredients:
                                    </strong>

                                    {recipe.ingredients
                                        ?.length > 0 ? (
                                        <ul>
                                            {recipe.ingredients.map(
                                                (
                                                    ingredient,
                                                    index
                                                ) => (
                                                    <li key={index}>
                                                        {
                                                            ingredient.name
                                                        }

                                                        {ingredient.amount
                                                        != null
                                                            ? ` — ${ingredient.amount}`
                                                            : ""}

                                                        {ingredient.unit
                                                            ? ` ${ingredient.unit}`
                                                            : ""}
                                                    </li>
                                                )
                                            )}
                                        </ul>
                                    ) : (
                                        <p>
                                            No ingredients
                                        </p>
                                    )}
                                </div>
                            </article>
                        ))
                    )}
                </section>
            )}
        </main>
    );
}