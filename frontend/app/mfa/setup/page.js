"use client";

import { useState } from "react";

const API_URL =
    process.env.NEXT_PUBLIC_API_URL ||
    "http://172.17.222.129:8080";

export default function MfaSetupPage() {
    const [qrCode, setQrCode] = useState("");
    const [code, setCode] = useState("");
    const [message, setMessage] = useState("");

    async function setupMfa() {
        const response = await fetch(
            `${API_URL}/auth/mfa/setup`,
            {
                method: "POST",
                credentials: "include",
            }
        );

        const data = await response.json();

        setQrCode(data.qrCode);
    }

    async function enableMfa() {
        const response = await fetch(
            `${API_URL}/auth/mfa/enable`,
            {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({
                    code,
                }),
            }
        );

        if (!response.ok) {
            setMessage("Invalid code");
            return;
        }

        setMessage("MFA enabled successfully");
    }

    return (
        <main>
            <h1>Enable MFA</h1>

            {!qrCode && (
                <button
                    className="app-button"
                    onClick={setupMfa}>
                    Generate QR code
                </button>
            )}

            {qrCode && (
                <>
                    <p>
                        Scan this QR code with
                        Google Authenticator and enter the
                        code below.
                    </p>

                    <img
                        src={`data:image/png;base64,${qrCode}`}
                        alt="MFA QR code"
                    />

                    <input
                        type="text"
                        placeholder="123456"
                        value={code}
                        onChange={(event) =>
                            setCode(event.target.value)
                        }
                    />

                    <button
                        className="app-button"
                        onClick={enableMfa}>
                        Enable MFA
                    </button>
                </>
            )}

            {message && (
                <p>{message}</p>
            )}
        </main>
    );
}