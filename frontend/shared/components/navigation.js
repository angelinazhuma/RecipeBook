"use client";

import Link from "next/link";
import {
    useEffect,
    useState,
} from "react";
import {
    usePathname,
    useRouter,
} from "next/navigation";

import {
    isAuthenticated,
    logoutUser,
} from "@/shared/services/authService";

export default function Navigation() {
    const router = useRouter();
    const pathname = usePathname();

    const [isLoggedIn, setIsLoggedIn] =
        useState(false);

    // сhecks the token every time the page changes
    useEffect(() => {
        setIsLoggedIn(isAuthenticated());
    }, [pathname]);

    const handleLogout = () => {
        logoutUser();
        setIsLoggedIn(false);

        router.replace("/login");
        router.refresh();
    };

    return (
        <nav className="navigation">
            {isLoggedIn ? (
                <>
                    <Link href="/view">
                        View recipes
                    </Link>

                    <Link href="/add">
                        Add recipe
                    </Link>

                    <button
                        type="button"
                        className="logout-button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                </>
            ) : (
                <>
                    <Link href="/login">
                        Login
                    </Link>

                    <Link href="/register">
                        Register
                    </Link>
                </>
            )}
        </nav>
    );
}