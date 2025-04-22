import React from 'react';

function Header({ userInfo, onLogout }) {
    return (
        <header className="header">
                <div className="logo-container">
                    <img src="/logo.png" alt="Logo" className="logo" />
                </div>
                <h1 className="site-name">VulnNotes</h1>
            <div className="user-info">
                {userInfo.username && (
                    <>
                        <div>Пользователь: <strong>{userInfo.username}</strong></div>
                        <div>Роли: {userInfo.roles.join(', ')}</div>
                        <button onClick={onLogout} className="logout-button">Выйти</button>
                    </>
                )}
            </div>
        </header>
    );
}

export default Header;
