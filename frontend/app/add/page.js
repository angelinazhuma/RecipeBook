"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

import Form from "./components/form";

import {
    getToken,
} from "@/shared/services/authService";

export default function AddRecipePage() {
    const router = useRouter();

    useEffect(() => {
        // redirects to login if there is no token
        if (!getToken()) {
            router.replace("/login");
        }
    }, [router]);

    return <Form />;
}