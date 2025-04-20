import React, { useState, useEffect } from 'react';
import './App.css'; // <-- Подключаем наш CSS

function App() {
    const [notes, setNotes] = useState([]);
    const [newNote, setNewNote] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [authenticated, setAuthenticated] = useState(false);
    const [userInfo, setUserInfo] = useState({ username: '', roles: [] });

    const fetchNotes = async () => {
        try {
            const response = await fetch('/api/notes', { credentials: 'include' });
            if (response.ok) {
                const data = await response.json();
                setNotes(data);
            } else {
                console.error('Ошибка загрузки заметок.');
            }
        } catch (error) {
            console.error('Ошибка при загрузке заметок:', error);
        }
    };

    const checkAuth = async () => {
        try {
            const response = await fetch('/api/auth/me', { credentials: 'include' });
            if (response.ok) {
                const user = await response.json();
                console.log('Пользователь залогинен:', user.username, user.roles);
                setAuthenticated(true);
                setUserInfo({ username: user.username, roles: user.roles.map(role => role.authority) });
                fetchNotes();
            } else {
                setAuthenticated(false);
            }
        } catch (error) {
            console.error('Ошибка авторизации:', error);
            setAuthenticated(false);
        }
    };

    useEffect(() => {
        checkAuth();
    }, []);

    const handleLogin = async (event) => {
        event.preventDefault();

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
                await checkAuth();
            } else {
                console.error('Ошибка логина.');
                setAuthenticated(false);
            }
        } catch (error) {
            console.error('Ошибка при логине:', error);
            setAuthenticated(false);
        }
    };

    const handleLogout = async () => {
        try {
            const response = await fetch('/logout', {
                method: 'POST',
                credentials: 'include'
            });

            if (response.ok) {
                console.log('Успешный logout');
                setAuthenticated(false);
                setUserInfo({ username: '', roles: [] });
                setNotes([]);
            } else {
                console.error('Ошибка logout');
            }
        } catch (error) {
            console.error('Ошибка при logout:', error);
        }
    };

    const handleCreateNote = async (event) => {
        event.preventDefault();
        if (!newNote.trim()) return;

        try {
            const response = await fetch('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content: newNote }),
                credentials: 'include'
            });

            if (response.ok) {
                setNewNote('');
                fetchNotes();
            } else {
                console.error('Ошибка создания заметки.');
            }
        } catch (error) {
            console.error('Ошибка при создании заметки:', error);
        }
    };

    const handleDeleteNote = async (id) => {
        if (!window.confirm('Удалить заметку?')) return;

        try {
            const response = await fetch(`/api/notes/${id}`, {
                method: 'DELETE',
                credentials: 'include'
            });

            if (response.ok) {
                fetchNotes();
            } else {
                console.error('Ошибка удаления заметки.');
            }
        } catch (error) {
            console.error('Ошибка при удалении заметки:', error);
        }
    };

    const currentDate = new Date().toLocaleDateString();

    return (
    <div className="app">
    {/* Header */}
    <header className="header">
    <div className="logo-container">
    <img src="/logo.png" alt="Logo" className="logo" />
    <h1 className="site-name">Сервис Заметок</h1>
    </div>

    {authenticated && (
    <div className="user-info">
    <div>Пользователь: <b>{userInfo.username}</b></div>
    <div>Роли: <b>{userInfo.roles.join(', ')}</b></div>
    <button onClick={handleLogout} className="logout-button">Выйти</button>
    </div>
    )}
</header>

{/* Main */}
<main className="main">
{!authenticated ? (
<form onSubmit={handleLogin} className="form-card">
<h2>Вход</h2>
<input
type="text"
placeholder="Имя пользователя"
value={username}
onChange={(e) => setUsername(e.target.value)}
autoComplete="username"
className="input"
/>
<input
type="password"
placeholder="Пароль"
value={password}
onChange={(e) => setPassword(e.target.value)}
autoComplete="current-password"
className="input"
/>
<button type="submit" className="button">Войти</button>
</form>
) : (
<div className="notes-section">
<form onSubmit={handleCreateNote} className="note-form">
<h2>Создать новую заметку</h2>
<div className="note-form-input">
<input
type="text"
placeholder="Введите текст заметки"
value={newNote}
onChange={(e) => setNewNote(e.target.value)}
className="input"
/>
<button type="submit" className="button">Создать</button>
</div>
</form>

<h2>Список заметок</h2>
<ul className="note-list">
{notes.map((note) => (
<li key={note.id} className="note-item">
<div dangerouslySetInnerHTML={{ __html: note.content }} />
<button onClick={() => handleDeleteNote(note.id)} className="delete-button">Удалить</button>
</li>
))}
</ul>
</div>
)}
</main>

{/* Footer */}
<footer className="footer">
Текущая дата: {currentDate}
</footer>
</div>
);
}

export default App;