import {
    currentUserRequest,
} from "../api/authApi";

export async function getCurrentUser() {
    return currentUserRequest();
}