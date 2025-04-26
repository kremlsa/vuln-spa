export async function fetchWithErrorHandling(url, options = {}) {
    const response = await fetch(url, {
        credentials: 'include',
        ...options,
    });

    const contentType = response.headers.get('content-type');

    if (!response.ok) {
        let message = `Ошибка: ${response.status}`;
        if (contentType && contentType.includes('application/json')) {
            const err = await response.json();
            message = err.error || message;
        } else {
            const errText = await response.text();
            message = errText || message;
        }

        console.error("Ошибка HTTP запроса:", message);
        throw new Error(message); // <-- ОБЯЗАТЕЛЬНО выбросить исключение!
    }

    if (contentType && contentType.includes('application/json')) {
        return await response.json();
    } else {
        return await response.text();
    }
}