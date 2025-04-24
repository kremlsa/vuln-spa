import React from 'react';

function NoteForm({ newNote, setNewNote, onCreate }) {
    const handleSubmit = (event) => {
        event.preventDefault();
        onCreate(newNote);
        setNewNote('');
    };

    return (
        <form onSubmit={handleSubmit} className="note-form">
            <input
                type="text"
                placeholder="Введите текст заметки"
                value={newNote}
                onChange={(e) => setNewNote(e.target.value)}
                className="input"
            />
            <button type="submit" className="button">Создать</button>
        </form>
    );
}

export default NoteForm;
