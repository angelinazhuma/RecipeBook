export function getCsrfToken() {
    const cookies = document.cookie.split(";");

    const csrfCookie = cookies.find((cookie) =>
        cookie.trim().startsWith("XSRF-TOKEN=")
    );

    if (!csrfCookie) {
        return null;
    }

    return decodeURIComponent(
        csrfCookie
            .trim()
            .substring("XSRF-TOKEN=".length)
    );
}