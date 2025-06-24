// src/components/Footer.js
import React from 'react';
import './Footer.css';
import FooterDownload from './FooterDownload';

function Footer() {
  return (
    <footer className="footer">
      <div className="footer-content">
          © 2025 Vulnerable Application. https://gitlab.com/alexander.kremlev/vulnerable-spa.
          © 2025 Vulnerable Application. Alexander Kremlev.
      </div>
      <FooterDownload />
    </footer>
  );
}

export default Footer;
