import React from 'react';
import RegistrationForm from '../components/RegistrationForm';
import './RegistrationPage.css';

export default function RegistrationPage({ currentTheme, toggleTheme }) {
  return (
    <div className={`registration-wrapper ${currentTheme}`}>
      <RegistrationForm
        currentTheme={currentTheme}
        toggleTheme={toggleTheme}
      />
    </div>
  );
}