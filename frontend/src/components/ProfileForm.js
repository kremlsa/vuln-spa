// src/components/ProfileForm.js
import React, { useEffect, useState } from 'react';
import { fetchWithAuth } from '../utils/fetchWithAuth';
import './ProfileForm.css';

export default function ProfileForm({ navigate, currentTheme }) {
  const [profile, setProfile] = useState(null);
  const [error, setError] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [message, setMessage] = useState('');

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetchWithAuth('/api/profile', {}, navigate);
        if (!response) return;
        const data = await response.json();
        setProfile(data);
      } catch (err) {
        console.error(err);
        setError('Ошибка загрузки профиля');
      }
    };
    fetchProfile();
  }, [navigate]);

  const handlePasswordChange = async (e) => {
    e.preventDefault();
    setError('');
    setMessage('');
    try {
      const response = await fetchWithAuth('/api/profile/password', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ oldPassword, newPassword })
      }, navigate);
      if (response?.ok) {
        setMessage('Пароль успешно обновлён');
        setOldPassword('');
        setNewPassword('');
      } else {
        const text = await response.text();
        setError(text || 'Ошибка при смене пароля');
      }
    } catch (err) {
      setError('Сетевая ошибка');
    }
  };

  if (!profile) return <div className="profile-loading">Загрузка профиля...</div>;

  return (
    <div className={`profile-form ${currentTheme}`}>
      <h2 className="profile-title">Профиль пользователя</h2>
      <div className="profile-info">
        <p><strong>Имя:</strong> {profile.username}</p>
        <p><strong>Статус:</strong> {profile.enabled ? '🟢 Активен' : '🔴 Неактивен'}</p>
        <p><strong>VIP:</strong> {profile.isVip ? '💎 Да' : '🚫 Нет'}</p>
      </div>

      <form className="password-form" onSubmit={handlePasswordChange}>
        <h3>Сменить пароль</h3>
        {error && <div className="error">{error}</div>}
        {message && <div className="success">{message}</div>}

        <label>Старый пароль</label>
        <input
          type="password"
          value={oldPassword}
          onChange={(e) => setOldPassword(e.target.value)}
          required
        />

        <label>Новый пароль</label>
        <input
          type="password"
          value={newPassword}
          onChange={(e) => setNewPassword(e.target.value)}
          required
        />

        <button type="submit" className="update-button">Обновить пароль</button>
      </form>
    </div>
  );
}
