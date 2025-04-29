import React, { useState } from 'react';

function FooterDownload() {
  const [filename, setFilename] = useState('credits.md');

  const handleDownload = async () => {
    try {
    const response = await fetch(`/api/files?filename=${filename}`, {
      method: 'GET',
      credentials: 'include' // <-- добавляем это
    });
      if (!response.ok) throw new Error('Ошибка при загрузке');
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = filename;
      link.click();
    } catch (err) {
      console.error('Ошибка при загрузке файла', err);
    }
  };

  return (
    <div style={{ marginTop: '10px' }}>
      <label htmlFor="file-select">📄 Скачать файл: </label>
      <select
        id="file-select"
        value={filename}
        onChange={(e) => setFilename(e.target.value)}
        style={{ marginRight: '10px' }}
      >
        <option value="credits.md">credits.md</option>
        <option value="readme.md">readme.md</option>
      </select>
      <button onClick={handleDownload}>Скачать</button>
    </div>
  );
}

export default FooterDownload;
