// src/components/LoginForm.js
import React, { useState } from 'react';
import './LoginForm.css';

function LoginForm({ toggleTheme, currentTheme }) {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await fetch('/logout', { method: 'POST', credentials: 'include' });

      const response = await fetch('/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: new URLSearchParams({ username, password }),
        credentials: 'include'
      });

      if (response.ok) {
        window.location.href = '/';
      } else {
        alert('Неверные данные');
      }
    } catch (error) {
      console.error('Ошибка входа:', error);
    }
  };

  return (
    <form onSubmit={handleSubmit} className="login-form">
      <h1>Вход</h1>

      <label>Имя пользователя</label>
      <input
        type="text"
        value={username}
        onChange={(e) => setUsername(e.target.value)}
        className="login-input"
        required
      />

      <label>Пароль</label>
      <input
        type="password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
        className="login-input"
        required
      />

      <button type="submit" className="login-button">
        Войти
      </button>

      <button type="button" onClick={toggleTheme} className="toggle-theme-button">
        Переключить на {currentTheme === 'light' ? 'тёмную' : 'светлую'} тему
      </button>
    </form>
  );
}

export default LoginForm;
