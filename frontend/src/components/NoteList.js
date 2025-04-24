import React from 'react';

function NoteList({ notes, onDelete }) {
    return (
        <ul>
            {notes.map((note) => (
                <li key={note.id} className="note-list">
                    <div dangerouslySetInnerHTML={{ __html: note.content }} />
                    {onDelete && (
                        <button onClick={() => onDelete(note.id)} className="delete-button">
                            Удалить
                        </button>
                    )}
                </li>
            ))}
        </ul>
    );
}

export default NoteList;

