import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/Header';
import Footer from '../components/Footer';
import NoteForm from '../components/NoteForm';
import NoteList from '../components/NoteList';
import SearchNotes from '../components/SearchNotes';
import { fetchWithErrorHandling } from '../utils/fetchWithErrorHandling';

function NotesPage({ userInfo }) {
    const [notes, setNotes] = useState([]);
    const [newNote, setNewNote] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const canCreate = userInfo.roles.includes('ROLE_USER') || userInfo.roles.includes('ROLE_ADMIN');
    const canDelete = userInfo.roles.includes('ROLE_ADMIN');

    const fetchNotes = async () => {
        const data = await fetchWithErrorHandling('/api/notes', {}, setError);
        if (data) setNotes(data);
    };

    useEffect(() => {
        fetchNotes();
    }, []);

    const handleCreateNote = async (content) => {
        if (!content.trim()) return;

        const response = await fetchWithErrorHandling('/api/notes', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ content }),
        }, setError);

        if (response) {
            setNewNote('');
            fetchNotes();
        }
    };

    const handleDeleteNote = async (id) => {
        if (!window.confirm('Удалить заметку?')) return;

        const result = await fetchWithErrorHandling(`/api/notes/${id}`, {
            method: 'DELETE',
        }, setError);

        if (result !== null) {
            fetchNotes();
        }
    };

    const handleLogout = async () => {
        const result = await fetchWithErrorHandling('/logout', {
            method: 'POST',
        }, setError);

        if (result !== null) {
            navigate('/login');
        }
    };

    return (
        <div className="app">
            <Header userInfo={userInfo} onLogout={handleLogout} />
            <main className="main">
                {error && <div className="error-banner">⚠️ {error}</div>}

                {canCreate &&
                    <NoteForm newNote={newNote} setNewNote={setNewNote} onCreate={handleCreateNote} />
                }

                <NoteList notes={notes} onDelete={canDelete ? handleDeleteNote : null} />
                <SearchNotes />
            </main>
            <Footer />
        </div>
    );
}

export default NotesPage;
