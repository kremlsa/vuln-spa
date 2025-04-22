import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import NotesPage from './pages/NotesPage';
import './App.css';

function App() {
    const [authenticated, setAuthenticated] = useState(false);
    const [userInfo, setUserInfo] = useState({ username: '', roles: [] });
    const [loadingAuth, setLoadingAuth] = useState(true); // ← добавляем флаг загрузки

    const checkAuth = async () => {
        try {
            const response = await fetch('/api/auth/me', { credentials: 'include' });
            if (response.ok) {
                const user = await response.json();
                setAuthenticated(true);
                setUserInfo({ username: user.username, roles: user.roles.map(role => role.authority) });
            } else {
                setAuthenticated(false);
            }
        } catch (error) {
            console.error('Ошибка авторизации:', error);
            setAuthenticated(false);
        } finally {
            setLoadingAuth(false); // ← в любом случае, заканчиваем загрузку
        }
    };

    useEffect(() => {
        checkAuth();
    }, []);

    if (loadingAuth) {
        return <div className="loading">Загрузка...</div>; // ← пока грузится авторизация
    }

    return (
        <Router>
            <Routes>
                <Route path="/login" element={<LoginPage setAuthenticated={setAuthenticated} setUserInfo={setUserInfo} />} />
                <Route path="/notes" element={authenticated ? <NotesPage userInfo={userInfo} /> : <Navigate to="/login" />} />
                <Route path="*" element={<Navigate to={authenticated ? "/notes" : "/login"} />} />
            </Routes>
        </Router>
    );
}

export default App;
