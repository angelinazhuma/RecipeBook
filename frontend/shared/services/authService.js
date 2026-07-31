import {
    loginRequest,
    registerRequest,
} from "../api/authApi";

// logs in the user and saves the JWT token
export async function loginUser(credentials) {
    const userData = await loginRequest(credentials);

    localStorage.setItem(
        "token",
        userData.token
    );

    return userData;
}

// Registers a new user
export async function registerUser(userData) {
    return registerRequest(userData);
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
