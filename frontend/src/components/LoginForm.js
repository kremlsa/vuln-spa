import React from 'react';

function LoginForm({ username, setUsername, password, setPassword, handleLogin }) {
    return (
    <form onSubmit={handleLogin} className="form-card">
    <h2>Вход</h2>
    <input
    type="text"
    placeholder="Имя пользователя"
    value={username}
    onChange={(e) => setUsername(e.target.value)}
    className="input"
    autoComplete="username"
    />
    <input
    type="password"
    placeholder="Пароль"
    value={password}
    onChange={(e) => setPassword(e.target.value)}
    className="input"
    autoComplete="current-password"
    />
    <button type="submit" className="button">Войти</button>
    </form>
    );
}

export default LoginForm;