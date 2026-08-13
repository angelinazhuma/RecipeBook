"use client";
import {
    useEffect,
    useState,
} from "react";
import {
    fetchAllUsers,
    fetchUserRecipes,
} from "@/shared/api/adminApi";

const USERS_PER_PAGE = 10;

export default function AdminPanel() {

    const [users, setUsers] = useState([]);
    const [recipes, setRecipes] = useState([]);
    const [selectedUser, setSelectedUser] = useState(null);
    // Spring pagination starts from page 0
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [totalUsers, setTotalUsers] = useState(0);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");
    const [search, setSearch] = useState("");

    useEffect(() => {

        async function loadUsers() {

            try {
                setLoading(true);
                setError("");

                const data =
                    await fetchAllUsers(
                        page,
                        USERS_PER_PAGE,
                        search
                    );
                /*
                 * Backend returns Page<User>
                 *
                 * data.content =
                 * users of the current page
                 */
                setUsers(data.content);

                /*
                 * Total number of pages
                 * is calculated by backend
                 */
                setTotalPages(
                    data.totalPages
                );
                /*
                 * Total number of users
                 * in the database
                 */
                setTotalUsers(
                    data.totalElements
                );

            } catch (err) {
                setError(
                    err.message ||
                    "Failed to load users"
                );
            } finally {

                setLoading(false);
            }
        }
        loadUsers();

    }, [page, search]);

    /*
     * Loads recipes of selected user.
     */
    async function handleUserClick(user) {
        try {
            setError("");

            const data =
                await fetchUserRecipes(
                    user.id
                );

            setSelectedUser(user);
            setRecipes(data);

            window.scrollTo({
                top: 0,
                behavior: "smooth",
            });

        } catch (err) {
            setError(
                err.message ||
                "Failed to load recipes"
            );
        }
    }


    function closeRecipes() {

        setSelectedUser(null);
        setRecipes([]);
    }

    function previousPage() {

        if (page > 0) {
            setPage(page - 1);
        }
    }
    /*
     * Next backend page.
     */
    function nextPage() {

        if (page < totalPages - 1) {
            setPage(page + 1);
        }
    }

    function handleSearch(event) {
        setSearch(event.target.value);
        setPage(0);
    }

    return (

        <main className="admin-page">

            <div className="admin-container">

                <div className="admin-header">
                    <div>
                        <h1>
                            Admin panel
                        </h1>

                        <p>
                            Manage users and
                            view their recipes
                        </p>
                    </div>

                    <div className="admin-user-count">

                        {totalUsers} users

                    </div>
                </div>


                {/* SELECTED USER RECIPES */}

                {selectedUser && (

                    <section className="admin-recipes-section">

                        <div className="admin-recipes-header">
                            <div>
                                <h2>
                                    Recipes of{" "}
                                    {
                                        selectedUser.username
                                    }
                                </h2>

                                <p>

                                    {recipes.length}{" "}

                                    {
                                        recipes.length === 1
                                            ? "recipe"
                                            : "recipes"
                                    }

                                </p>
                            </div>


                            <button
                                type="button"
                                className="admin-close-button"
                                onClick={closeRecipes}
                            >
                                Close
                            </button>
                        </div>

                        {recipes.length === 0 ? (

                            <p className="empty">
                                No recipes
                            </p>

                        ) : (

                            <div className="recipes-grid">

                                {recipes.map(
                                    (recipe) => (

                                        <article
                                            key={recipe.id}
                                            className="recipe-card"
                                        >
                                            <h3>
                                                {recipe.name}
                                            </h3>

                                            <p>
                                                <strong>
                                                    Author:
                                                </strong>{" "}

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

                                                {
                                                    recipe.createdAt
                                                        ? new Date(
                                                            recipe.createdAt
                                                        )
                                                            .toLocaleDateString()
                                                        : ""
                                                }
                                            </p>

                                            <h4>
                                                Ingredients
                                            </h4>

                                            <ul>

                                                {
                                                    recipe.ingredients
                                                        ?.map(
                                                            (
                                                                ingredient,
                                                                index
                                                            ) => (

                                                                <li
                                                                    key={
                                                                        index
                                                                    }
                                                                >
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
                                                        )
                                                }

                                            </ul>

                                        </article>
                                    )
                                )}

                            </div>
                        )}
                    </section>
                )}

                {/* USERS */}
                <section className="admin-users-card">
                    <div className="admin-users-top">

                        <div>
                            <h2>
                                Users
                            </h2>

                            <p>
                                Search by username or email
                            </p>
                        </div>

                        <input
                            className="admin-search"
                            type="search"
                            placeholder="Search users..."
                            value={search}
                            onChange={handleSearch}
                        />

                    </div>
                    {loading && (

                        <p className="admin-status">
                            Loading users...
                        </p>

                    )}

                    {error && (

                        <p className="error-message">
                            {error}
                        </p>

                    )}

                    {!loading &&
                        !error &&
                        users.length === 0 && (

                            <p className="admin-status">
                                No users found
                            </p>

                        )}

                    {!loading &&
                        users.length > 0 && (
                            <>
                                <div className="admin-table">
                                    <div className="admin-table-header">

                                        <span>
                                            Username
                                        </span>
                                        <span>
                                            Email
                                        </span>
                                        <span>
                                            Role
                                        </span>
                                        <span>
                                            Action
                                        </span>
                                    </div>

                                    {users.map(
                                        (user) => (

                                            <div
                                                className="admin-user-row"
                                                key={user.id}
                                            >
                                                <div className="admin-user-name">
                                                    <div className="admin-avatar">

                                                        {
                                                            user.username
                                                                ?.charAt(0)
                                                                .toUpperCase()
                                                        }

                                                    </div>
                                                    <span>
                                                        {
                                                            user.username
                                                        }
                                                    </span>
                                                </div>

                                                <span className="admin-email">

                                                    {user.email}

                                                </span>

                                                <span
                                                    className={
                                                        user.role ===
                                                        "ADMIN"
                                                            ? "admin-role admin-role-admin"
                                                            : "admin-role"
                                                    }
                                                >
                                                    {user.role}

                                                </span>

                                                <button
                                                    type="button"
                                                    className="admin-view-button"
                                                    onClick={() =>
                                                        handleUserClick(
                                                            user
                                                        )
                                                    }
                                                >
                                                    View recipes
                                                </button>
                                            </div>
                                        )
                                    )}
                                </div>


                                {/* BACKEND PAGINATION */}

                                {totalPages > 1 && ( // show only if pages are more than 1

                                    <div className="admin-pagination">

                                        <button
                                            type="button"
                                            disabled={
                                                page === 0
                                            }
                                            onClick={
                                                previousPage
                                            }
                                        >
                                            Previous

                                        </button>

                                        <span>

                                            Page{" "}
                                            {page + 1}{" "}
                                            of{" "}
                                            {totalPages}

                                        </span>

                                        <button
                                            type="button"
                                            disabled={
                                                page >=
                                                totalPages - 1
                                            }
                                            onClick={
                                                nextPage
                                            }
                                        >

                                            Next

                                        </button>
                                    </div>
                                )}
                            </>
                        )}
                </section>
            </div>

        </main>

    );
}