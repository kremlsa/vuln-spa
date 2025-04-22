import React, { useState, useEffect } from 'react';

function Footer() {
    const [currentDate, setCurrentDate] = useState('');

    useEffect(() => {
        const today = new Date();
        const formattedDate = today.toLocaleDateString();
        setCurrentDate(formattedDate);
    }, []); // Пустой массив, чтобы запустить только один раз при монтировании

    return (
        <footer className="footer">
            Текущая дата: {currentDate}
        </footer>
    );
}

export default Footer;