// src/utils/fetchWithAuth.js
export async function fetchWithAuth(url, options = {}, navigate) {
  const response = await fetch(url, {
    ...options,
    credentials: 'include',
  });

  if (response.status === 401) {
    navigate('/login', { state: { sessionExpired: true } });
    return null;
  }

  return response;
}
