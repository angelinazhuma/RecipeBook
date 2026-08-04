// Registers a new user
import {registerRequest} from "@/shared/api/authApi";

export async function registerUser(userData) {
    return registerRequest(userData);
}