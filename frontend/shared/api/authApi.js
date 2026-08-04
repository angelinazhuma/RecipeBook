import {getCsrfToken} from "../utils/csrfToken";

const API_URL =
    process.env.NEXT_PUBLIC_API_URL ||
    "http://172.17.222.129:8080";

export async function loginRequest(credentials) {
    const response = await fetch(
        `${API_URL}/auth/login`,
        {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(credentials),
        }
    );

    return handleResponse(
        response,
        "Login failed"
    );
}

export async function registerRequest(userData) {
    const response = await fetch(
        `${API_URL}/auth/register`,
        {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(userData),
        }
    );

    return handleResponse(
        response,
        "Registration failed"
    );
}

export async function logoutRequest() {
    const csrfToken = getCsrfToken();

    const response = await fetch(

        `${API_URL}/auth/logout`,
        {
            method: "POST",
            credentials: "include",

            headers: {
                "X-XSRF-TOKEN": csrfToken,
            },
        }
    );

    if (!response.ok) {
        return {
            success: false,
            error: "Logout failed",
        };
    }

    return {
        success: true,
    };
}

export async function currentUserRequest() {
    const response = await fetch(
        `${API_URL}/auth/me`,
        {
            credentials: "include",
        }
    );

    if (response.status === 401) {
        return {
            authenticated: false,
            user: null,
        };
    }

    if (!response.ok) {
        return {
            authenticated: false,
            user: null,
        };
    }

    return {
        authenticated: true,
        user: await response.json(),
    };
}

async function handleResponse(
    response,
    fallbackMessage
) {
    if (!response.ok) {
        const errorText =
            await response.text();

        return {
            success: false,
            data: null,
            error:
                errorText ||
                fallbackMessage,
        };
    }

    return {
        success: true,
        data: await response.json(),
        error: null,
    };
}