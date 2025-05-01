// src/components/RegistrationForm.js
import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './RegistrationForm.css';

export default function RegistrationForm({ toggleTheme, currentTheme }) {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      const resp = await fetch('/api/auth/register', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(form),
      });
      if (!resp.ok) {
        const text = await resp.text();
        setError(text || 'Ошибка при регистрации');
        return;
      }
      alert('Регистрация прошла успешно! Пожалуйста, войдите.');
      navigate('/login');
    } catch (err) {
      console.error(err);
      setError('Сетевая ошибка, попробуйте позже');
    }
  };

  return (
    <div className="registration-wrapper">
      <form
        onSubmit={handleSubmit}
        className={`registration-form ${currentTheme}`}
      >
        <h2 className="registration-title">Регистрация</h2>
        {error && <div className="error shake">{error}</div>}

        <label className="registration-label">Логин</label>
        <input
          name="username"
          className="registration-input"
          value={form.username}
          onChange={handleChange}
          required
        />

        <label className="registration-label">Пароль</label>
        <input
          name="password"
          type="password"
          className="registration-input"
          value={form.password}
          onChange={handleChange}
          required
        />

        <button type="submit" className="registration-button">
          Зарегистрироваться
        </button>

        <button
          type="button"
          onClick={toggleTheme}
          className="toggle-theme-button"
        >
          Переключить на {currentTheme === 'light' ? 'тёмную' : 'светлую'} тему
        </button>

        <div className="login-link">
          Уже есть аккаунт? <Link to="/login">Войти</Link>
        </div>
      </form>
    </div>
  );
}
