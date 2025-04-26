// src/components/SearchNotes.js
import React, { useState } from 'react';
import './SearchNotes.css';

function SearchNotes() {
  const [searchQuery, setSearchQuery] = useState('');
  const [results, setResults] = useState([]);
  const [searched, setSearched] = useState(false); // <<< Новый флаг "искали ли уже?"

  const handleSearch = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`/api/search?query=${encodeURIComponent(searchQuery)}`, {
        credentials: 'include',
      });
      if (response.ok) {
        const data = await response.json();
        setResults(data);
        setSearched(true); // <<< Отмечаем что поиск был
      }
    } catch (error) {
      console.error('Ошибка поиска:', error);
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

      {searched && (
        <>
          {results.length > 0 ? (
            <ul className="search-results fade-in">
              {results.map((note) => (
                <li key={note.id}>{note.content}</li>
              ))}
            </ul>
          ) : (
            <div className="no-results">Ничего не найдено</div>
          )}
        </>
      )}
    </div>
  );
}

export default SearchNotes;
