import React, { useState, useEffect } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage';
import RegistrationPage from './pages/RegistrationPage';
import NotesPage from './pages/NotesPage';
import ProtectedRoute from './components/ProtectedRoute';
import ProfilePage from './pages/ProfilePage';
import './App.css';
import './AppTheme.css';

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState({ username: '', roles: [], isVip: false });
  const [loadingAuth, setLoadingAuth] = useState(true);
  // Тема приложения
  const [theme, setTheme] = useState('light');
  const toggleTheme = () => setTheme(prev => (prev === 'light' ? 'dark' : 'light'));

  const checkAuth = async () => {
    try {
      const response = await fetch('/api/auth/me', { credentials: 'include' });
      if (response.ok) {
        const user = await response.json();
        setAuthenticated(true);
        setUserInfo({
          username: user.username,
          roles: user.roles || [],
          isVip: user.isVip || false
        });
      } else {
        setAuthenticated(false);
      }
    } catch {
      setAuthenticated(false);
    } finally {
      setLoadingAuth(false);
    }
  };

  useEffect(() => {
    checkAuth();
  }, []);

  if (loadingAuth) {
    return <div className="loading">Загрузка...</div>;
  }

  return (
    <Router>
      <Routes>
        <Route
          path="/login"
          element={
            <LoginPage
              setAuthenticated={setAuthenticated}
              setUserInfo={setUserInfo}
              currentTheme={theme}
              toggleTheme={toggleTheme}
            />
          }
        />
        <Route
          path="/register"
          element={
            <RegistrationPage
              currentTheme={theme}
              toggleTheme={toggleTheme}
            />
          }
        />
        <Route
          path="/notes"
          element={
            <ProtectedRoute authenticated={authenticated}>
              <NotesPage
               currentTheme={theme}
               toggleTheme={toggleTheme}
               userInfo={userInfo} />
            </ProtectedRoute>
          }
        />
        <Route
          path="/profile"
          element={
            <ProtectedRoute authenticated={authenticated}>
              <ProfilePage
                currentTheme={theme}
                toggleTheme={toggleTheme}
               />
            </ProtectedRoute>
          }
        />
        <Route
          path="*"
          element={<Navigate to={authenticated ? "/notes" : "/login"} />}
        />
      </Routes>
    </Router>
  );
}

export default App;
