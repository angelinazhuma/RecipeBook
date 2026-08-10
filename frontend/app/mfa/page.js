"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

import {
    verifyMfa,
} from '@/shared/api/mfaApi';

export default function MfaPage() {
    const router = useRouter();

    const [code, setCode] = useState("");
    const [error, setError] = useState("");

    async function verify() {
        setError("");

        const mfaToken =
            sessionStorage.getItem("mfaToken");

        if (!mfaToken) {
            setError("MFA session expired");
            return;
        }

        try {
            const result = await verifyMfa(
                mfaToken,
                code
            );

            if (!result.success) {
                setError(
                    result.error ||
                    "Invalid code"
                );
                return;
            }

            sessionStorage.removeItem(
                "mfaToken"
            );

            router.replace("/view");
            router.refresh();

        } catch (error) {
            setError("MFA verification failed");
        }
    }

    return (
        <main>
            <h1>
                Two-Factor Authentication
            </h1>

            <input
                type="text"
                placeholder="123456"
                value={code}
                maxLength={6}
                onChange={(event) =>
                    setCode(event.target.value)
                }
            />

            <button
                className="app-button"
                onClick={verify}>
                Verify
            </button>

            {error && (
                <p>{error}</p>
            )}
        </main>
    );
}