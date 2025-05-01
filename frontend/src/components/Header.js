// src/components/Header.js
import React, { useState, useEffect } from 'react';
import VipRequestButton from './VipRequestButton';
import VipStatusBanner from './VipStatusBanner';
import './Header.css';

function Header({ userInfo, onLogout, theme, isVip, setIsVip }) {
  const [visible, setVisible] = useState(true);
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

        {/* Центральный блок — вывод VIP статуса */}
        <div className="header-center">
        <VipStatusBanner isVip={isVip}/>
        </div>

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
          {/* Вставим кнопку VIP-проверки */}
          {userInfo?.username && (
            <VipRequestButton username={userInfo.username} setIsVip={setIsVip}/>
          )}
          <button className="logout-button" onClick={onLogout}>
            Выйти
          </button>
        </div>
      </div>
    </header>
  );
}

export default Header;
