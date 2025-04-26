// src/components/Header.js
import React from 'react';
import './Header.css';

function Header({ userInfo, onLogout, theme }) {
  const getInitial = (username) => {
    if (!username) return '?';
    return username.charAt(0).toUpperCase();
  };

  return (
    <header className={`header ${theme}`}>
      <div className="header-content">
        {/* Левый блок — Логотип + Название */}
        <div className="header-left">
          <div ><img src="/logo.png" alt="Logo" className="logo" /></div>
          <div className="site-title">Vulnerable SPA</div>
        </div>

        {/* Центральный блок-пустышка */}
        <div className="header-center"></div>

        {/* Правый блок — Юзер + Кнопка */}
        <div className="header-right">
          <div className="user-info-block">
            <div className="user-avatar pulse">
              {getInitial(userInfo?.username)}
            </div>
            <div className="user-info">
              <div className="user-name">{userInfo?.username}</div>
              <div className="user-role">{userInfo?.roles?.[0] || 'ROLE_USER'}</div>
            </div>
          </div>
          <button className="logout-button" onClick={onLogout}>
            Выйти
          </button>
        </div>
      </div>
    </header>
  );
}

export default Header;
