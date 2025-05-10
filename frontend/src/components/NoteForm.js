import React, { useState } from 'react';
import { fetchWithErrorHandling } from '../utils/fetchWithErrorHandling';
import ErrorBanner from './ErrorBanner';

function NoteForm({ fetchNotes, username }) {
    const [newNote, setNewNote] = useState('');
    const [errorMessage, setErrorMessage] = useState(null);

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (!newNote.trim()) return;

        try {
            await fetchWithErrorHandling('/api/notes', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                credentials: 'include',
                body: JSON.stringify({
                    content: newNote,
                    author: username
                }),
            });

            setNewNote('');
            setErrorMessage(null); // Очистить ошибку после успеха
            fetchNotes(); // Обновить список заметок
        } catch (error) {
            const msg = error.message || 'Ошибка отправки запроса';
            const isWaf = msg.toLowerCase().includes('waf') || msg.toLowerCase().includes('запрещено');

            setErrorMessage({ type: isWaf ? 'waf' : 'general', text: msg });
        }
    };

    return (
    <>
        <form onSubmit={handleSubmit} className="note-form">
            <h2>Создать новую заметку</h2>
            <input
                type="text"
                placeholder="Введите текст заметки"
                value={newNote}
                onChange={(e) => setNewNote(e.target.value)}
                className="input"
            />
            <button type="submit" className="note-submit-button">Создать</button>
        </form>
        <ErrorBanner message={errorMessage} />
    </>
    );
}

export default NoteForm;
