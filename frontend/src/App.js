import React, { useState, useEffect } from 'react';
import Header from './components/Header';
import Footer from './components/Footer';
import LoginForm from './components/LoginForm';
import NotesSection from './components/NotesSection';
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
    <Header authenticated={authenticated} userInfo={userInfo} handleLogout={handleLogout} />
    <main className="main">
    {!authenticated ? (
    <LoginForm
    username={username}
    setUsername={setUsername}
    password={password}
    setPassword={setPassword}
    handleLogin={handleLogin}
    />
    ) : (
    <NotesSection
    notes={notes}
    newNote={newNote}
    setNewNote={setNewNote}
    handleCreateNote={handleCreateNote}
    handleDeleteNote={handleDeleteNote}
    />
    )}
</main>
<Footer currentDate={currentDate} />
</div>
);
}

export default App;