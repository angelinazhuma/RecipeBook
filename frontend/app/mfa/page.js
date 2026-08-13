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
        <main className="mfa-verify-page">

            <div className="mfa-verify-card">

                <h1>
                    Two-Factor Authentication
                </h1>

                <p className="mfa-verify-description">
                    Enter the 6-digit code from
                    Google Authenticator.
                </p>

                <div className="mfa-verify-form">

                    <label htmlFor="mfa-code">
                        Verification code
                    </label>

                    <input
                        id="mfa-code"
                        type="text"
                        inputMode="numeric"
                        placeholder="1 2 3 4 5 6"
                        value={code}
                        maxLength={6}
                        onChange={(event) =>
                            setCode(
                                event.target.value
                            )
                        }
                    />

                    <button
                        className="mfa-verify-button"
                        onClick={verify}
                    >
                        Verify
                    </button>

                </div>

                {error && (
                    <p className="mfa-error">
                        {error}
                    </p>
                )}

            </div>

        </main>
    );}