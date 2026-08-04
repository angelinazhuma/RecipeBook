import {
    logoutRequest,
} from "../api/authApi";

export async function logoutUser() {
    return logoutRequest();
}