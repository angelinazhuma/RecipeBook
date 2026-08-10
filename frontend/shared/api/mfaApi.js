const API_URL =
    process.env.NEXT_PUBLIC_API_URL ||
    "http://172.17.222.129:8080";

export async function verifyMfa(
    mfaToken,
    code
) {
    const response = await fetch(
        `${API_URL}/auth/mfa/verify-login`,
        {
            method: "POST",

            credentials: "include",

            headers: {
                "Content-Type":
                    "application/json",
            },

            body: JSON.stringify({
                mfaToken,
                code,
            }),
        }
    );

    if (!response.ok) {
        const errorText =
            await response.text();

        return {
            success: false,
            error:
                errorText ||
                "Invalid MFA code",
        };
    }

    return {
        success: true,
    };
}