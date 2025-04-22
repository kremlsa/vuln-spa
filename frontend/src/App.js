import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import NotesPage from './pages/NotesPage';
import './App.css';

function App() {
    const [authenticated, setAuthenticated] = useState(false);
    const [userInfo, setUserInfo] = useState({ username: '', roles: [] });

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
        }
    };

    useEffect(() => {
        checkAuth();
    }, []);

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
