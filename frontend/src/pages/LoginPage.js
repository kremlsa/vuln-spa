import React, { useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import './LoginPage.css';

function LoginPage({ setAuthenticated, setUserInfo }) {
  const location = useLocation();
  const sessionExpired = location.state?.sessionExpired;

  const [theme, setTheme] = useState('light');
  const toggleTheme = () => {
    setTheme(prev => (prev === 'light' ? 'dark' : 'light'));
  };

  return (
    <div className={`login-wrapper ${theme}-theme`}>
      {sessionExpired && (
        <div className="session-expired-message">
          Ваша сессия истекла. Пожалуйста, войдите снова.
        </div>
      )}
      <LoginForm
        setAuthenticated={setAuthenticated}
        setUserInfo={setUserInfo}
        toggleTheme={toggleTheme}
        currentTheme={theme}
      />
    </div>
  );
}

export default LoginPage;