import React, { useState } from 'react';

function SearchNotes() {
    const [searchQuery, setSearchQuery] = useState('');
    const [results, setResults] = useState([]);

    const handleSearch = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`/api/search?query=${encodeURIComponent(searchQuery)}`, {
                credentials: 'include'
            });
            if (response.ok) {
                const data = await response.json();
                setResults(data);
            }
        } catch (error) {
            console.error('Ошибка поиска:', error);
        }
    };

 return (
        <div className="form-card">
            <h2>Поиск заметок</h2>
            <form onSubmit={handleSearch}>
                <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder="Введите текст"
                    className="input"
                />
                <button type="submit" className="button">Искать</button>
            </form>

            {results.length > 0 && (
                <ul className="note-list">
                    {results.map(note => (
                        <li key={note.id} className="note-item">
                            <div dangerouslySetInnerHTML={{ __html: note.content }} />
                        </li>
                    ))}
                </ul>
            )}

            {results.length === 0 && <p>Результатов нет.</p>}
        </div>
    );
}

export default SearchNotes;
