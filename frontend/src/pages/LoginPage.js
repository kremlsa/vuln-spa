import React, { useState } from 'react';
import { useLocation, Link } from 'react-router-dom';
import LoginForm from '../components/LoginForm';
import './LoginPage.css';

function LoginPage({ setAuthenticated, setUserInfo, currentTheme, toggleTheme }) {
  const location = useLocation();
  const sessionExpired = location.state?.sessionExpired;

  return (
    <div className={`login-wrapper ${currentTheme}`}>
      {sessionExpired && (
        <div className="session-expired-message">
          Ваша сессия истекла. Пожалуйста, войдите снова.
        </div>
      )}
      <LoginForm
        setAuthenticated={setAuthenticated}
        setUserInfo={setUserInfo}
        toggleTheme={toggleTheme}
        currentTheme={currentTheme}
      />
    </div>
  );
}

export default LoginPage;