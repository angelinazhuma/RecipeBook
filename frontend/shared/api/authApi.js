const apiUrl = "http://172.17.222.129:8080";


export async function loginRequest(credentials) {
    const response = await fetch(`${apiUrl}/auth/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(credentials),
    });

    if (!response.ok) {
        throw new Error("Login failed");
    }

    return response.json();
}

export async function registerRequest(userData) {
    const response = await fetch(`${apiUrl}/auth/register`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(userData),
    });

    if (!response.ok) {
        throw new Error("Registration failed");
    }

    return response.json();
}