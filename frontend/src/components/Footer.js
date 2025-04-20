import React from 'react';

function Footer({ currentDate }) {
    return (
    <footer className="footer">
    Текущая дата: {currentDate}
</footer>
);
}

export default Footer;