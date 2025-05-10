import React from 'react';
import './ErrorBanner.css'; // Подключи стили отдельно

function ErrorBanner({ message }) {
    if (!message || !message.text) return null;

    const isWaf = message.type === 'waf';

    return (
        <div className={`error-banner ${isWaf ? 'waf-error' : ''}`}>
            ⚠️ {message.text}
            {isWaf && (
                <>
                    <div className="waf-hint">
                        Ваш запрос был заблокирован системой защиты (WAF).
                        Не пытайтесь повторно — Чак Норрис наблюдает.
                    </div>
                    <img
                        src="/images/chuck.gif"
                        alt="Chuck Norris is watching"
                        className="chuck-norris-gif"
                    />
                </>
            )}
        </div>
    );
}

export default ErrorBanner;