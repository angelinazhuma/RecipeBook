"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

import Form from "./components/Form";

import {
    getCurrentUser,
} from "@/shared/services/currentUserService";

export default function AddRecipePage() {
    const router = useRouter();

    const [isCheckingAuth, setIsCheckingAuth] =
        useState(true);

    useEffect(() => {
        let cancelled = false;

        getCurrentUser().then((result) => {
            if (cancelled) {
                return;
            }

            if (!result.authenticated) {
                router.replace("/login");
                return;
            }

            setIsCheckingAuth(false);
        });

        return () => {
            cancelled = true;
        };
    }, [router]);

    if (isCheckingAuth) {
        return (
            <section className="card">
                <p>Checking authentication...</p>
            </section>
        );
    }

    return <Form />;
}