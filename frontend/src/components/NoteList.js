import React from 'react';

function NoteList({ notes, onDelete, currentUsername, isAdmin }) {
    return (
        <ul className="note-list">
            {notes.map(note => (
                <li key={note.id}>
                    <div className="note-author">Автор: {note.author || "Неизвестно"}</div>
                    <div className="note-content" dangerouslySetInnerHTML={{ __html: note.content }} />
                        {onDelete && (note.author === currentUsername || isAdmin) && (
                            <button onClick={() => onDelete(note.id)}>Удалить</button>
                        )}
                </li>
            ))}
        </ul>
    );
}

export default NoteList;
