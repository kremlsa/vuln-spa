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

    return (
        <div>
            <Header userInfo={userInfo} onLogout={handleLogout} />
            <main style={{ padding: '20px' }}>
                <NoteForm newNote={newNote} setNewNote={setNewNote} onCreate={handleCreateNote} />
                <NoteList notes={notes} onDelete={handleDeleteNote} />
            </main>
            <Footer />
        </div>
    );
}

export default NotesPage;
