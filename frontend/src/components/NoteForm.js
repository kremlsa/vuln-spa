// Пишем автора на фронте и не проверяем на бэке = BAC
import React from 'react';

function NoteForm({ newNote, setNewNote, onCreate, username }) {
    const handleSubmit = (e) => {
        e.preventDefault();
        if (!newNote.trim()) return;

        // Отправляем сразу текст и имя автора
        onCreate({
            content: newNote,
            author: username
        });

        setNewNote(''); // Очищаем поле после создания
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
        </form>
    );
}

export default NoteForm;
