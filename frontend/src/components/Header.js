import React from 'react';

function Header({ authenticated, userInfo, handleLogout }) {
    return (
    <header className="header">
    <div className="logo-container">
    <img src="/logo.png" alt="Logo" className="logo" />
    <h1 className="site-name">VulnNotes</h1>
    </div>

    {authenticated && (
    <div className="user-info">
    <div>Пользователь: <b>{userInfo.username}</b></div>
    <div>Роли: <b>{userInfo.roles.join(', ')}</b></div>
    <button onClick={handleLogout} className="logout-button">Выйти</button>
    </div>
    )}
</header>
);
}

export default Header;