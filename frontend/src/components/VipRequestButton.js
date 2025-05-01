// src/components/VipRequestButton.js
import React from 'react';

export default function VipRequestButton({ setIsVip }) {
  const sendVipRequest = async () => {
    try {
      const response = await fetch('/api/vip/status', {
        method: 'GET',
        credentials: 'include'
      });

      if (response.ok) {
        const { isVip } = await response.json();
        setIsVip(isVip);
      } else {
        if (response.status === 401) {
          // Сессия истекла или неавторизован
          alert('Пожалуйста, войдите в систему, чтобы проверить VIP-статус.');
        } else {
          console.error('Ошибка VIP-запроса, статус:', response.status);
        }
      }
    } catch (error) {
      console.error('Ошибка VIP-запроса:', error);
    }
  };

  return (
    <button
      onClick={sendVipRequest}
      className="button"
      title="Проверить VIP-доступ"
    >
      Проверить VIP
    </button>
  );
}
