import React, { useState, useEffect } from 'react';

function App() {
    const [notes, setNotes] = useState([]);
    const [newNote, setNewNote] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [authenticated, setAuthenticated] = useState(false);

    // Загрузка заметок
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

    // Проверка, залогинен ли пользователь
    useEffect(() => {
        const checkAuth = async () => {
            try {
                const response = await fetch('/api/auth/me', { credentials: 'include' });
                if (response.ok) {
                    const user = await response.json();
                    console.log('Пользователь залогинен:', user.username, user.roles);
                    setAuthenticated(true);
                    fetchNotes();
                } else {
                    setAuthenticated(false);
                }
            } catch (error) {
                console.error('Ошибка авторизации:', error);
                setAuthenticated(false);
            }
        };

        checkAuth();
    }, []);

    // Логин
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
                const authCheck = await fetch('/api/auth/me', { credentials: 'include' });
                if (authCheck.ok) {
                    const user = await authCheck.json();
                    console.log('Логин успешный:', user.username);
                    setAuthenticated(true);
                    fetchNotes();
                } else {
                    console.error('Не авторизован после логина');
                    setAuthenticated(false);
                }
            } else {
                console.error('Ошибка логина.');
                setAuthenticated(false);
            }
        } catch (error) {
            console.error('Ошибка при логине:', error);
            setAuthenticated(false);
        }
    };

    // Logout
    const handleLogout = async () => {
        try {
            const response = await fetch('/logout', {
                method: 'POST',
                credentials: 'include'
            });

            if (response.ok) {
                console.log('Успешный logout');
                setAuthenticated(false);
                setNotes([]);
            } else {
                console.error('Ошибка logout');
            }
        } catch (error) {
            console.error('Ошибка при logout:', error);
        }
    };

    // Создание заметки
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

    // Удаление заметки
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

    return (
    <div className="App">
    <h1>Сервис Заметок</h1>

    {!authenticated ? (
    <form onSubmit={handleLogin}>
    <h2>Вход</h2>
    <input
    type="text"
    placeholder="Username"
    value={username}
    onChange={(e) => setUsername(e.target.value)}
    autoComplete="username"
    />
    <input
    type="password"
    placeholder="Password"
    value={password}
    onChange={(e) => setPassword(e.target.value)}
    autoComplete="current-password"
    />
    <button type="submit">Login</button>
    </form>
    ) : (
    <div>
    <button onClick={handleLogout}>Logout</button>

    <form onSubmit={handleCreateNote}>
    <h2>Создать новую заметку</h2>
    <input
    type="text"
    placeholder="Введите текст заметки"
    value={newNote}
    onChange={(e) => setNewNote(e.target.value)}
    />
    <button type="submit">Создать</button>
    </form>

    <h2>Список заметок</h2>
    <ul>
    {notes.map((note) => (
    <li key={note.id}>
    <div dangerouslySetInnerHTML={{ __html: note.content }} />
    {authenticated && (
    <button onClick={() => handleDeleteNote(note.id)}>Удалить</button>
    )}
</li>
))}
</ul>
</div>
)}
</div>
);
}

export default App;
