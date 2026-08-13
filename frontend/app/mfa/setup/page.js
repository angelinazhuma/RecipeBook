"use client";

import { useState } from "react";

const API_URL =
    process.env.NEXT_PUBLIC_API_URL ||
    "http://172.17.222.129:8080";

export default function MfaSetupPage() {

    const [qrCode, setQrCode] =
        useState("");

    const [code, setCode] =
        useState("");

    const [message, setMessage] =
        useState("");

    async function setupMfa() {

        const response = await fetch(
            `${API_URL}/auth/mfa/setup`,
            {
                method: "POST",
                credentials: "include",
            }
        );

        const data =
            await response.json();

        setQrCode(data.qrCode);
    }

    async function enableMfa() {

        const response = await fetch(
            `${API_URL}/auth/mfa/enable`,
            {
                method: "POST",
                credentials: "include",

                headers: {
                    "Content-Type":
                        "application/json",
                },

                body: JSON.stringify({
                    code,
                }),
            }
        );

        if (!response.ok) {
            setMessage(
                "Invalid code"
            );
            return;
        }

        setMessage(
            "MFA enabled successfully"
        );
    }

    return (
        <main className="mfa-page">

            <div className="mfa-card">

                <h1>
                    Enable MFA
                </h1>

                {!qrCode && (

                    <div className="mfa-start">

                        <p className="mfa-description">
                            Add an extra layer of
                            security to your account
                            with two-factor authentication.
                        </p>

                        <button
                            className="app-button"
                            onClick={setupMfa}
                        >
                            Generate QR code
                        </button>

                    </div>
                )}

                {qrCode && (
                    <>
                        <p className="mfa-description">
                            Scan this QR code with
                            Google Authenticator and
                            enter the 6-digit code below.
                        </p>

                        <div className="mfa-qr-container">

                            <img
                                className="mfa-qr"
                                src={
                                    `data:image/png;base64,${qrCode}`
                                }
                                alt="MFA QR code"
                            />

                        </div>

                        <div className="mfa-form">

                            <label htmlFor="mfa-code">
                                Verification code
                            </label>

                            <input
                                id="mfa-code"
                                type="text"
                                inputMode="numeric"
                                maxLength={6}
                                placeholder="1 2 3 4 5 6"
                                value={code}
                                onChange={(event) =>
                                    setCode(
                                        event.target.value
                                    )
                                }
                            />

                            <button
                                className="mfa-enable-button"
                                onClick={enableMfa}
                            >
                                Enable MFA
                            </button>
                        </div>
                    </>
                )}

                {message && (
                    <p className="mfa-message">
                        {message}
                    </p>
                )}

            </div>
        </main>
    );
}