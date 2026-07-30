"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";

import {
    logoutUser,
} from "@/shared/services/authService";

export default function Navigation() {
    const router = useRouter();

    // Removes the token and opens the login page
    const handleLogout = () => {
        logoutUser();
        router.push("/login");
    };

    return (
        <nav className="navigation">
            <Link href="/view">
                View recipes
            </Link>

            <Link href="/add">
                Add recipe
            </Link>

            <Link href="/login">
                Login
            </Link>

            <Link href="/register">
                Register
            </Link>

            <button
                type="button"
                className="secondary-button"
                onClick={handleLogout}
            >
                Logout
            </button>
        </nav>
    );
}
