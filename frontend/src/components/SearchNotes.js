import React, { useState } from 'react';
import { fetchWithErrorHandling } from '../utils/fetchWithErrorHandling';

function SearchNotes() {
    const [searchQuery, setSearchQuery] = useState('');
    const [results, setResults] = useState([]);
    const [error, setError] = useState(null);

    const handleSearch = async (e) => {
        e.preventDefault();
        setError(null);
        setResults([]);

        if (!searchQuery.trim()) {
            setError("Введите текст для поиска");
            return;
        }

        const data = await fetchWithErrorHandling(
            `/api/search?query=${encodeURIComponent(searchQuery)}`,
            {},
            setError
        );

        if (data) setResults(data);
    };

    return (
        <div className="form-card">
            <h2>🔍 Поиск заметок</h2>
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

            {error && <div className="error-banner">⚠️ {error}</div>}

            {results.length > 0 && (
                <ul className="note-list">
                    {results.map(note => (
                        <li key={note.id} className="note-item">
                            <div dangerouslySetInnerHTML={{ __html: note.content }} />
                        </li>
                    ))}
                </ul>
            )}

            {results.length === 0 && searchQuery && !error && (
                <p>Результатов нет.</p>
            )}
        </div>
    );
}

export default SearchNotes;
