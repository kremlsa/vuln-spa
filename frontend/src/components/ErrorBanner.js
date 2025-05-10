import React, { useEffect } from 'react';
import './ErrorBanner.css';

function ErrorBanner({ message, onClear }) {
    if (!message || !message.text) return null;

    const isWaf = message.type === 'waf';

      // Автоочистка через 5 секунд
      useEffect(() => {
        const timer = setTimeout(() => {
          if (onClear) onClear();
        }, 10000);

        return () => clearTimeout(timer);
      }, [message, onClear]);

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