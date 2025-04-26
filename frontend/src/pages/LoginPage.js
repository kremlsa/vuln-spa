// src/pages/LoginPage.js
import React, { useState } from 'react';
import { useLocation } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import './LoginPage.css'; // обычные стили

function LoginPage() {
  const location = useLocation();
  const sessionExpired = location.state?.sessionExpired;

  const [theme, setTheme] = useState('light'); // состояние темы

  const toggleTheme = () => {
    setTheme((prev) => (prev === 'light' ? 'dark' : 'light'));
  };

  return (
    <div className={`login-wrapper ${theme}-theme`}>
      {sessionExpired && (
        <div className="session-expired-message">
          Ваша сессия истекла. Пожалуйста, войдите снова.
        </div>
      )}
      <LoginForm toggleTheme={toggleTheme} currentTheme={theme} />
    </div>
  );
}

export default LoginPage;
