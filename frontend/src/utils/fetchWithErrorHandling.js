// src/utils/fetchWithErrorHandling.js

export async function fetchWithErrorHandling(url, options = {}, setError = null) {
    try {
        const response = await fetch(url, {
            credentials: 'include', // если нужна авторизация по cookie
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

            if (setError) setError(message);
            return null;
        }

        // Возвращаем JSON или текст, в зависимости от content-type
        if (contentType && contentType.includes('application/json')) {
            return await response.json();
        } else {
            return await response.text();
        }

    } catch (err) {
        if (setError) setError("Ошибка соединения с сервером");
        console.error("Сетевая ошибка:", err);
        return null;
    }
}
