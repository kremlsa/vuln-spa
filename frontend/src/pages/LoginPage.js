import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

function LoginPage({ setAuthenticated, setUserInfo }) {

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (event) => {
        event.preventDefault();

        try {
            // Явный logout перед login
            await fetch('/logout', {
                method: 'POST',
                credentials: 'include'
            });
        } catch (error) {
            console.warn('Ошибка logout перед login (может быть нормой, если не было активной сессии).');
        }


        const formData = new URLSearchParams();
        formData.append('username', username);
        formData.append('password', password);

        try {
            const response = await fetch('/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: formData,
                credentials: 'include'
            });

            if (response.ok) {
                const authCheck = await fetch('/api/auth/me', { credentials: 'include' });
                if (authCheck.ok) {
                    const user = await authCheck.json();
                    setAuthenticated(true);
                    setUserInfo({ username: user.username, roles: user.roles.map(r => r.authority) });
                    navigate('/notes');
                } else {
                    alert('Ошибка проверки авторизации');
                }
            } else {
                alert('Ошибка авторизации');
            }
        } catch (error) {
            console.error('Ошибка логина:', error);
            alert('Ошибка соединения');
        }
    };

    return (
        <div style={{ textAlign: 'center', marginTop: '100px' }}>
            <h2>Вход в VulnNotes</h2>
            <form onSubmit={handleLogin}>
                <input
                    type="text"
                    placeholder="Имя пользователя"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    autoComplete="username"
                    style={{ marginBottom: '10px' }}
                /><br/>
                <input
                    type="password"
                    placeholder="Пароль"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    autoComplete="current-password"
                    style={{ marginBottom: '10px' }}
                /><br/>
                <button type="submit">Войти</button>
            </form>
        </div>
    );
}

export default LoginPage;
