"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Link from "next/link";

import {
    loginUser,
} from "@/shared/services/authService";

export default function LoginForm() {
    const router = useRouter();

    const [login, setLogin] = useState("");
    const [password, setPassword] =
        useState("");

    const [error, setError] = useState("");
    const [isLoading, setIsLoading] =
        useState(false);

    async function handleSubmit(event) {
        event.preventDefault();

        setError("");
        setIsLoading(true);

        try {
            await loginUser({
                login,
                password,
            });

            router.replace("/view");
            router.refresh();
        } catch (err) {
            setError(
                err.message || "Login failed"
            );
        } finally {
            setIsLoading(false);
        }
    }

    return (
        <>
            <form
                className="auth-form"
                onSubmit={handleSubmit}
            >
                <label htmlFor="login">
                    Username or email

                    <input
                        id="login"
                        type="text"
                        placeholder="Username or email"
                        value={login}
                        onChange={(event) =>
                            setLogin(
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
                        required
                    />
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
                    disabled={isLoading}
                >
                    {isLoading
                        ? "Logging in..."
                        : "Login"}
                </button>
            </form>

            <p className="auth-link">
                Don&apos;t have an account?{" "}
                <Link href="/register">
                    Register
                </Link>
            </p>
        </>
    );
}