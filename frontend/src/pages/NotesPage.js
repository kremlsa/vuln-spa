// src/pages/NotesPage.js
import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import NoteForm from '../components/NoteForm';
import NoteList from '../components/NoteList';
import SearchNotes from '../components/SearchNotes';
import ErrorBanner from '../components/ErrorBanner';
import { fetchWithAuth } from '../utils/fetchWithAuth';
import './NotesPage.css';

function NotesPage({ userInfo, currentTheme, toggleTheme }) {
  const [notes, setNotes] = useState([]);
  const [error, setError] = useState(null);
  const [loaded, setLoaded] = useState(false); // для fade-in анимации
  const navigate = useNavigate();
  const isAdmin = userInfo.roles?.includes('ROLE_ADMIN') ?? false;

  // Инициализация isVip из userInfo
  const [isVip, setIsVip] = useState(userInfo.isVip);

  // Синхронизация при изменении userInfo.isVip
  useEffect(() => {
    setIsVip(userInfo.isVip);
  }, [userInfo.isVip]);

  // Проверка прав на создание и удаление заметок
  const canCreate =
    userInfo.roles?.some(r => r === 'ROLE_USER' || r === 'ROLE_ADMIN') ?? false;
  const canDelete = userInfo.roles?.includes('ROLE_ADMIN') ?? false;

  const fetchNotes = async () => {
    try {
      const response = await fetchWithAuth('/api/notes', {}, navigate);
      if (response) {
        const data = await response.json();
        setNotes(data);
        setLoaded(true); // После загрузки показать анимацию
      }
    } catch (err) {
      setError('Ошибка загрузки заметок');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchNotes();
  }, [navigate]);

  const handleCreateNote = async (noteData) => {
    if (!noteData.content.trim()) return;
    try {
      const response = await fetch('/api/notes', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(noteData),
      });
      if (response.ok) {
        fetchNotes();
      }
    } catch (err) {
      console.error('Ошибка при создании заметки:', err);
    }
  };

const handleDeleteNote = async (id) => {
  if (!window.confirm('Удалить заметку?')) return;
  try {
    const response = await fetchWithAuth(
      `/api/notes/${id}`,
      { method: 'DELETE' },
      navigate
    );

    if (!response.ok) {
      const data = await response.json();
      const msg = data.error || 'Ошибка удаления заметки';
      const isChuck = msg.toLowerCase().includes('чак') || msg.toLowerCase().includes('chuck');

      setError({ type: isChuck ? 'waf' : 'general', text: msg });
      return;
    }

    fetchNotes();
  } catch (err) {
    const msg = err.message || 'Ошибка удаления заметки';
    const isChuck = msg.toLowerCase().includes('чак') || msg.toLowerCase().includes('chuck');

    setError({ type: isChuck ? 'waf' : 'general', text: msg });
    console.error(err);
  }
};

  const handleLogout = async () => {
    try {
      const response = await fetchWithAuth('/logout', { method: 'POST' }, navigate);
      if (response) {
        navigate('/login');
      }
    } catch (err) {
      setError('Ошибка выхода');
      console.error(err);
    }
  };

  return (
    <div className={`app ${currentTheme}`}>
      <Header
        userInfo={userInfo}
        onLogout={handleLogout}
        currentTheme={currentTheme}
        isVip={isVip}
        setIsVip={setIsVip}
      />
      <main className={`main ${loaded ? 'fade-in' : ''}`}>
        <div className="theme-toggle">
          <button onClick={toggleTheme}>
            {currentTheme === 'light' ? 'Тёмная тема' : 'Светлая тема'}
          </button>
        </div>

        <ErrorBanner message={error} onClear={() => setError(null)} />

        {canCreate && (
          <NoteForm fetchNotes={fetchNotes} username={userInfo.username} />
        )}

        <NoteList
          notes={notes}
          onDelete={handleDeleteNote}
          currentUsername={userInfo.username}
          isAdmin={isAdmin}
        />

        <SearchNotes />
      </main>
      <Footer />
    </div>
  );
}

export default NotesPage;
