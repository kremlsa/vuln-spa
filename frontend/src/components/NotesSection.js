import React from 'react';

function NotesSection({ notes, newNote, setNewNote, handleCreateNote, handleDeleteNote }) {
    return (
    <div className="notes-section">
    <form onSubmit={handleCreateNote} className="note-form">
    <h2>Создать новую заметку</h2>
    <div className="note-form-input">
    <input
    type="text"
    placeholder="Введите текст заметки"
    value={newNote}
    onChange={(e) => setNewNote(e.target.value)}
    className="input"
    />
    <button type="submit" className="button">Создать</button>
    </div>
    </form>

    <h2>Список заметок</h2>
    <ul className="note-list">
    {notes.map((note) => (
    <li key={note.id} className="note-item">
    <div dangerouslySetInnerHTML={{ __html: note.content }} />
    <button onClick={() => handleDeleteNote(note.id)} className="delete-button">Удалить</button>
    </li>
    ))}
</ul>
</div>
);
}

export default NotesSection;
