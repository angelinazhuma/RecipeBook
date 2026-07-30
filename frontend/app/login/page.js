"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import {
    loginUser,
} from "@/shared/services/authService";

export default function LoginPage() {
    const router = useRouter();

    const [login, setLogin] = useState("");
    const [password, setPassword] =
        useState("");

    const [error, setError] = useState("");
    const [isLoading, setIsLoading] =
        useState(false);

    // Sends the login form
    const handleSubmit = async (event) => {
        event.preventDefault();

        // Removes the previous error
        setError("");

        if (!login.trim() || !password) {
            setError(
                "Enter your username/email and password"
            );
            return;
        }

        try {
            // Disables the button during the request
            setIsLoading(true);

            await loginUser(
                login.trim(),
                password
            );

            // Opens the recipes page after login
            router.push("/view");
        } catch (error) {
            setError(error.message);
        } finally {
            // Enables the button after the request
            setIsLoading(false);
        }
    };

    return (
        <section className="card">
            <h2>Login</h2>

            <form
                className="form"
                onSubmit={handleSubmit}
            >
                <label htmlFor="login">
                    Username or email
                </label>

                <input
                    id="login"
                    type="text"
                    placeholder="Username or email"
                    value={login}
                    onChange={(event) =>
                        setLogin(event.target.value)
                    }
                />

                <label htmlFor="password">
                    Password
                </label>

                <input
                    id="password"
                    type="password"
                    placeholder="Password"
                    value={password}
                    onChange={(event) =>
                        setPassword(event.target.value)
                    }
                />

                {error && (
                    <p role="alert">
                        {error}
                    </p>
                )}

                <button
                    type="submit"
                    className="add-button"
                    disabled={isLoading}
                >
                    {isLoading
                        ? "Logging in..."
                        : "Login"}
                </button>
            </form>
        </section>
    );
}