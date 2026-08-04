"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

import {
    registerUser,
} from "@/shared/services/registerService";

export default function RegisterForm() {
    const router = useRouter();

    const [username, setUsername] =
        useState("");
    const [email, setEmail] =
        useState("");
    const [password, setPassword] =
        useState("");

    const [error, setError] =
        useState("");
    const [loading, setLoading] =
        useState(false);

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setLoading(true);

        try {
            await registerUser({
                username,
                email,
                password,
            });

            router.replace("/login");
        } catch (err) {
            setError(
                err.message ||
                "Registration failed"
            );
        } finally {
            setLoading(false);
        }
    }

    return (
        <>
            <form
                onSubmit={handleSubmit}
                className="auth-form"
            >
                <label htmlFor="username">
                    Username

                    <input
                        id="username"
                        placeholder="Username"
                        type="text"
                        value={username}
                        onChange={(event) =>
                            setUsername(
                                event.target.value
                            )
                        }
                        required
                    />
                </label>

                <label htmlFor="email">
                    Email

                    <input
                        id="email"
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(event) =>
                            setEmail(
                                event.target.value
                            )
                        }
                        required
                    />
                </label>

                <label htmlFor="password">
                    Password

                    <input
                        id="password"
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(event) =>
                            setPassword(
                                event.target.value
                            )
                        }
                        minLength={8}
                        required
                    />

                    <small className="password-hint">
                        Password must contain at
                        least 8 characters.
                    </small>
                </label>

                {error && (
                    <p
                        className="error-message"
                        role="alert"
                    >
                        {error}
                    </p>
                )}

                <button
                    type="submit"
                    disabled={loading}
                >
                    {loading
                        ? "Registering..."
                        : "Register"}
                </button>
            </form>

            <p className="auth-link">
                Already have an account?{" "}
                <Link href="/login">
                    Login
                </Link>
            </p>
        </>
    );
}