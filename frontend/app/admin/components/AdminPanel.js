"use client";

import { useEffect, useState } from "react";

import {
    fetchAllUsers,
    fetchUserRecipes,
} from "@/shared/api/adminApi";

export default function AdminPanel() {
    const [users, setUsers] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [selectedUser, setSelectedUser] =
        useState(null);

    useEffect(() => {
        async function loadUsers() {
            const data = await fetchAllUsers();
            setUsers(data);
        }

        loadUsers();
    }, []);

    async function handleUserClick(user) {
        const data =
            await fetchUserRecipes(user.id);

        setSelectedUser(user);
        setRecipes(data);
    }

    return (
        <main>
            <h1>Admin panel</h1>

            <h2>Users</h2>

            <div className="admin-users">
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
            </div>

            {selectedUser && (
                <section>
                    <h2>
                        Recipes of{" "}
                        {selectedUser.username}
                    </h2>

                    {recipes.length === 0 ? (
                        <p>No recipes</p>
                    ) : (
                        <div className="recipe-list">
                            {recipes.map((recipe) => (
                                <article
                                    key={recipe.id}
                                    className="card"
                                >
                                    <h3>{recipe.name}</h3>

                                    <p>
                                        <strong>Author:</strong>{" "}
                                        {recipe.author}
                                    </p>

                                    <p>
                                        <strong>
                                            Description:
                                        </strong>{" "}
                                        {
                                            recipe.recipeDescription
                                        }
                                    </p>

                                    <p>
                                        <strong>
                                            Created:
                                        </strong>{" "}
                                        {recipe.createdAt
                                            ? new Date(
                                                recipe.createdAt
                                            ).toLocaleDateString()
                                            : ""}
                                    </p>

                                    <h4>Ingredients</h4>

                                    <ul>
                                        {recipe.ingredients?.map(
                                            (
                                                ingredient,
                                                index
                                            ) => (
                                                <li key={index}>
                                                    {
                                                        ingredient.name
                                                    }{" "}
                                                    {
                                                        ingredient.amount
                                                    }{" "}
                                                    {
                                                        ingredient.unit
                                                    }
                                                </li>
                                            )
                                        )}
                                    </ul>
                                </article>
                            ))}
                        </div>
                    )}
                </section>
            )}
        </main>
    );
}