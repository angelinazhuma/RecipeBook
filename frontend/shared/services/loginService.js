import {
    loginRequest,
} from "../api/authApi";

// logs in the user and saves the JWT token
export async function loginUser(credentials) {
    return loginRequest(credentials);
}