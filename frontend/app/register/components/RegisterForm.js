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

    const [passwordStrength, setPasswordStrength] =
        useState(0);

    const hasLength = password.length >= 8;

    const hasUppercase =
        /[A-Z]/.test(password);

    const hasLowercase =
        /[a-z]/.test(password);

    const hasNumber =
        /\d/.test(password); //digit

    const hasSpecial =
        /[^A-Za-z0-9]/.test(password); // special character

    const isPasswordValid =
        hasLength &&
        hasUppercase &&
        hasLowercase &&
        hasNumber &&
        hasSpecial;

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

    function handlePasswordChange(event) {
        const value = event.target.value;

        setPassword(value);

        let point = 0; // number with 0, here will be result pf password strength

        if (value.length >= 8) {
            const tests = [
                /[0-9]/,
                /[a-z]/,
                /[A-Z]/,
                /[^0-9a-zA-Z]/,
            ];

            tests.forEach((test) => { //goes through all tests
                if (test.test(value)) {
                    point++; // point = point +1
                } //
            });
        }

        setPasswordStrength(point); // set point to password strength
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
                        onChange={handlePasswordChange}
                        minLength={8}
                        required
                    />

                    <span className="password-strength-label">
        Strength of password
    </span>
                    <div className="power-container">
                        <div
                            className="power-point"
                            style={{ '--strength': passwordStrength }}
                        />
                    </div>

                    <div className="password-rules">

                        <p>
                            {hasLength ? "✅" : "❌"} At least
                            8 characters
                        </p>

                        <p>
                            {hasUppercase ? "✅" : "❌"} One
                            uppercase letter
                        </p>

                        <p>
                            {hasLowercase ? "✅" : "❌"} One
                            lowercase letter
                        </p>

                        <p>
                            {hasNumber ? "✅" : "❌"} One
                            number
                        </p>

                        <p>
                            {hasSpecial ? "✅" : "❌"} One
                            special character
                        </p>

                    </div>


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
                    disabled={loading || !isPasswordValid}
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