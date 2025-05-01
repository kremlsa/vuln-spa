// src/pages/ProfilePage.js
import React from 'react';
import { useNavigate } from 'react-router-dom';
import ProfileForm from '../components/ProfileForm';
import './ProfilePage.css';

export default function ProfilePage({ currentTheme, toggleTheme }) {
  const navigate = useNavigate();

  return (
    <div className={`profile-page-wrapper ${currentTheme}`}>
      <ProfileForm navigate={navigate} currentTheme={currentTheme} />
      <div className="back-link">
        <button onClick={() => navigate('/notes')} className="back-button">
          ← Вернуться на главную
        </button>
      </div>
    </div>
  );
}
