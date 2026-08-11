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
    getCurrentUser,
} from "@/shared/services/currentUserService";

import {
    logoutUser,
} from "@/shared/services/logoutService";

export default function Navigation() {
    const router = useRouter();
    const pathname = usePathname();

    const [currentUser, setCurrentUser] =
        useState(null);

    const [isLoggedIn, setIsLoggedIn] =
        useState(false);

    const [isCheckingAuth, setIsCheckingAuth] =
        useState(true);

    // Checks authentication every time the page changes
    useEffect(() => {
        let cancelled = false;

        async function checkAuthentication() {
            setIsCheckingAuth(true);

            const result =
                await getCurrentUser();

            setIsLoggedIn(
                result.authenticated
            );

            if (result.authenticated) {
                setCurrentUser(result.user);
            } else {
                setCurrentUser(null);
            }

            setIsCheckingAuth(false);
        }

        checkAuthentication();

        return () => {
            cancelled = true;
        };
    }, [pathname]);

    const handleLogout = async () => {
        const result = await logoutUser();

        if (!result.success) {
            return;
        }

        setIsLoggedIn(false);

        router.replace("/login");
        router.refresh();
    };

    if (isCheckingAuth) {
        return null;
    }

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

                    {currentUser && !currentUser.mfaEnabled && (
                        <Link href="/mfa/setup">
                            Enable MFA
                        </Link>
                    )}

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

            {currentUser?.role === "ADMIN" && (
                <Link href="/admin">
                    Admin
                </Link>
            )}


        </nav>
    );
}