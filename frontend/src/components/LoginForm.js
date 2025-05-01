// src/components/LoginForm.js
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './LoginForm.css';

export default function LoginForm({
  setAuthenticated,
  setUserInfo,
  toggleTheme,
  currentTheme
}) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    try {
      // 1) POST /api/auth/login
      const loginResp = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify({ username, password })
      });

      if (!loginResp.ok) {
        setError('Неверное имя пользователя или пароль');
        return;
      }

      // 2) GET /api/auth/me
      const meResp = await fetch('/api/auth/me', {
        credentials: 'include'
      });
      if (!meResp.ok) {
        setError('Не удалось получить информацию о пользователе');
        return;
      }
      const user = await meResp.json();

      // 3) Обновляем состояние в App: сохраняем username, roles и isVip
      setAuthenticated(true);
      setUserInfo({
        username: user.username,
        roles: user.roles || [],
        isVip: user.isVip || false
      });

      // 4) Переходим на защищённую страницу
      navigate('/notes');
    } catch (err) {
      console.error('Ошибка входа:', err);
      setError('Сетевая ошибка, попробуйте ещё раз');
    }
  };

  return (
    <form onSubmit={handleSubmit} className={`login-form ${currentTheme}`}>
      <h1>Вход</h1>

      {error && <div className="error">{error}</div>}

      <label>Имя пользователя</label>
      <input
        type="text"
        value={username}
        onChange={e => setUsername(e.target.value)}
        className="login-input"
        required
      />

      <label>Пароль</label>
      <input
        type="password"
        value={password}
        onChange={e => setPassword(e.target.value)}
        className="login-input"
        required
      />

      <button type="submit" className="login-button">
        Войти
      </button>

      <button
        type="button"
        onClick={() => navigate('/register')}
        className="login-button"
      >
        Зарегистрироваться
      </button>

      <button
        type="button"
        onClick={toggleTheme}
        className="toggle-theme-button"
      >
        Переключить на {currentTheme === 'light' ? 'тёмную' : 'светлую'} тему
      </button>
    </form>
  );
}
