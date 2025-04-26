import React, { useState } from 'react';
import { fetchWithErrorHandling } from '../utils/fetchWithErrorHandling';

function NoteForm({ fetchNotes, username }) {
    const [newNote, setNewNote] = useState('');
    const [errorMessage, setErrorMessage] = useState('');

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
            setErrorMessage(''); // Очистить ошибку после успеха
            fetchNotes(); // Обновить список заметок
        } catch (error) {
            console.error('Ошибка при создании заметки:', error);
            setErrorMessage(error.message || 'Ошибка отправки запроса');
        }
    };

    return (
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
        {errorMessage && <div className="error-banner">⚠️ {errorMessage}</div>}
        </form>

    );
}

export default NoteForm;
