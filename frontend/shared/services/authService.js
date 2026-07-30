import {
    loginRequest,
    registerRequest,
} from "../api/authApi";

// logs in the user and saves the JWT token
export async function loginUser(
    login,
    password
) {
    const userData = await loginRequest(
        login,
        password
    );

    localStorage.setItem(
        "token",
        userData.token
    );

    return userData;
}

// Registers a new user
export async function registerUser(
    username,
    email,
    password
) {
    return registerRequest(
        username,
        email,
        password
    );
}

// returns the saved JWT token
export function getToken() {
    return localStorage.getItem("token");
}

// removes the JWT token
export function logoutUser() {
    localStorage.removeItem("token");
}

// checks whether the user has a token
export function isAuthenticated() {
    return getToken() !== null;
}
