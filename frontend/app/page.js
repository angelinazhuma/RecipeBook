"use client";

import { useEffect } from "react";
import { useRouter } from "next/navigation";

import {
    isAuthenticated,
} from "@/shared/services/authService";

export default function HomePage() {
    const router = useRouter();

    useEffect(() => {
        if (isAuthenticated()) {
            router.replace("/view");
        } else {
            router.replace("/login");
        }
    }, [router]);

    return null;
}