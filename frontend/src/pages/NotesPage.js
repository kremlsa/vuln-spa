import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import NoteForm from '../components/NoteForm';
import NoteList from '../components/NoteList';

function NotesPage({ userInfo }) {
    const [notes, setNotes] = useState([]);
    const [newNote, setNewNote] = useState('');
    const navigate = useNavigate();


    const fetchNotes = async () => {
        try {
            const response = await fetch('/api/notes', { credentials: 'include' });
            if (response.ok) {
                const data = await response.json();
                setNotes(data);
            }
        } catch (error) {
            console.error('Ошибка загрузки заметок:', error);
        }
    };

    useEffect(() => {
        fetchNotes();
    }, []);

    const handleCreateNote = async (content) => {
        if (!content.trim()) return;

        try {
            const response = await fetch('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content }),
                credentials: 'include'
            });

            if (response.ok) {
                fetchNotes();
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
            }
        } catch (error) {
            console.error('Ошибка при удалении заметки:', error);
        }
    };

    const handleLogout = async () => {
        try {
            const response = await fetch('/logout', {
                method: 'POST',
                credentials: 'include'
            });

            if (response.ok) {
                navigate('/login');
            }
        } catch (error) {
            console.error('Ошибка при выходе:', error);
        }
    };

    // Проверка прав
    const canCreate = userInfo.roles.includes('ROLE_USER') || userInfo.roles.includes('ROLE_ADMIN');
    const canDelete = userInfo.roles.includes('ROLE_ADMIN');

    return (
        <div className="app">
            <Header userInfo={userInfo} onLogout={handleLogout} />
            <main className="main">
                {/* Показываем форму создания только если есть право */}
                {canCreate && (
                    <NoteForm newNote={newNote} setNewNote={setNewNote} onCreate={handleCreateNote} />
                )}

                {/* Передаём флаг canDelete в NoteList */}
                <NoteList notes={notes} onDelete={canDelete ? handleDeleteNote : null} />
            </main>
            <Footer />
        </div>
    );
}


export default NotesPage;
