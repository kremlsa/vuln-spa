import React, { useState } from 'react';
import { fetchWithErrorHandling } from '../utils/fetchWithErrorHandling';
import ErrorBanner from './ErrorBanner';

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
        try {
            const data = await fetchWithErrorHandling(
                `/api/search?query=${encodeURIComponent(searchQuery)}`
            );
            if (data) setResults(data);
        } catch (err) {
            console.error('Ошибка поиска:', err);
            const msg = err.message || 'Ошибка поиска';
            const isWaf = msg.toLowerCase().includes('waf') || msg.toLowerCase().includes('запрещено');
            setError({ type: isWaf ? 'waf' : 'general', text: msg });
        }
    };

    return (
        <div className="search-notes">
            <h2>Поиск заметок</h2>
            <form onSubmit={handleSearch}>
                <input
                    type="text"
                    placeholder="Введите запрос..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="search-input"
                    required
                />
                <button type="submit" className="search-button">Найти</button>
            </form>

            <ErrorBanner message={error} />

            {results.length > 0 && (
                <ul className="search-results fade-in">
                    {results.map(note => (
                        <li key={note.id}>
                            <div dangerouslySetInnerHTML={{ __html: note.content }} />
                        </li>
                    ))}
                </ul>
            )}
            {results.length === 0 && searchQuery && !error && (
                <div className="no-results">Ничего не найдено</div>
            )}
        </div>
    );
}

export default SearchNotes;
