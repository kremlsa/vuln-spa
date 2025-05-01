import React from 'react';
import { Navigate } from 'react-router-dom';

/**
 * Если пользователь авторизован — рендерим детей,
 * иначе — редиректим на /login.
 */
export default function ProtectedRoute({ authenticated, children }) {
  return authenticated
    ? children
    : <Navigate to="/login" replace />;
}