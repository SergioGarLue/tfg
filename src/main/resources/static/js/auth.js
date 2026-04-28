/**
 * Auth Module - Gestión completa de JWT y autenticación
 * Proporciona funciones para almacenar, recuperar, verificar y gestionar tokens JWT
 */

const AUTH = (() => {
  const TOKEN_KEY = 'jwt_access_token';
  const REFRESH_TOKEN_KEY = 'jwt_refresh_token';
  const USER_KEY = 'user_authenticated';

  /**
   * Decodifica un JWT sin validar su firma (solo para lectura client-side)
   * NOTA: No es seguro validar la firma en el cliente, solo usamos esto para lectura
   */
  const decodeJWT = (token) => {
    try {
      const parts = token.split('.');
      if (parts.length !== 3) throw new Error('JWT inválido');

      const payload = parts[1];
      const base64 = payload.replace(/-/g, '+').replace(/_/g, '/');
      const padded = base64.padEnd(base64.length + (4 - (base64.length % 4)) % 4, '=');
      const decoded = JSON.parse(atob(padded));
      return decoded;
    } catch (error) {
      console.error('Error decodificando JWT:', error);
      return null;
    }
  };

  /**
   * Obtiene el token de acceso desde localStorage
   */
  const getAccessToken = () => {
    return localStorage.getItem(TOKEN_KEY);
  };

  /**
   * Obtiene el refresh token desde localStorage
   */
  const getRefreshToken = () => {
    return localStorage.getItem(REFRESH_TOKEN_KEY);
  };

  /**
   * Verifica si el token de acceso ha expirado
   */
  const isAccessTokenExpired = () => {
    const token = getAccessToken();
    if (!token) return true;

    const decoded = decodeJWT(token);
    if (!decoded || !decoded.exp) return true;

    const expirationTime = decoded.exp * 1000; // convertir a milisegundos
    const currentTime = Date.now();
    const bufferTime = 60 * 1000; // 1 minuto de margen

    return currentTime > (expirationTime - bufferTime);
  };

  /**
   * Verifica si el usuario está autenticado (tiene token válido)
   */
  const isAuthenticated = () => {
    const token = getAccessToken();
    return !!token && !isAccessTokenExpired();
  };

  /**
   * Obtiene los roles del token actual
   */
  const getUserRoles = () => {
    const token = getAccessToken();
    if (!token) return [];

    const decoded = decodeJWT(token);
    return decoded?.roles || [];
  };

  /**
   * Verifica si el usuario tiene un rol específico
   */
  const hasRole = (requiredRole) => {
    const roles = getUserRoles();
    return roles.includes(requiredRole);
  };

  /**
   * Obtiene la información del usuario autenticado
   */
  const getAuthenticatedUser = () => {
    try {
      const userStr = localStorage.getItem(USER_KEY);
      return userStr ? JSON.parse(userStr) : null;
    } catch (error) {
      console.error('Error obteniendo usuario autenticado:', error);
      return null;
    }
  };

  /**
   * Guarda los tokens y datos del usuario
   */
  const saveTokens = (accessToken, refreshToken, user) => {
    localStorage.setItem(TOKEN_KEY, accessToken);
    if (refreshToken) {
      localStorage.setItem(REFRESH_TOKEN_KEY, refreshToken);
    }
    if (user) {
      localStorage.setItem(USER_KEY, JSON.stringify(user));
    }
  };

  /**
   * Limpia todos los datos de autenticación
   */
  const clearTokens = () => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(REFRESH_TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
  };

  /**
   * Intenta renovar el token de acceso usando el refresh token
   */
  const refreshAccessToken = async () => {
    try {
      const refreshToken = getRefreshToken();
      if (!refreshToken) {
        clearTokens();
        return false;
      }

      const response = await fetch('/api/auth/refresh', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${refreshToken}`,
          'Content-Type': 'application/json'
        }
      });

      if (!response.ok) {
        clearTokens();
        return false;
      }

      const data = await response.json();
      localStorage.setItem(TOKEN_KEY, data.accessToken);
      return true;
    } catch (error) {
      console.error('Error renovando token:', error);
      clearTokens();
      return false;
    }
  };

  /**
   * Realiza login con usuario y contraseña
   */
  const login = async (username, password) => {
    try {
      const response = await fetch('/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
      });

      if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Error en el login');
      }

      const data = await response.json();
      saveTokens(data.accessToken, data.refreshToken, data.usuario);
      return data.usuario;
    } catch (error) {
      console.error('Error en login:', error);
      throw error;
    }
  };

  /**
   * Realiza logout del usuario
   */
  const logout = () => {
    clearTokens();
  };

  /**
   * Realiza registro de nuevo usuario
   */
  const register = async (username, email, password) => {
    try {
      const response = await fetch('/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username,
          correoElectronico: email,
          passwd: password
        })
      });

      if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Error en el registro');
      }

      return await response.text();
    } catch (error) {
      console.error('Error en registro:', error);
      throw error;
    }
  };

  // API pública del módulo
  return {
    getAccessToken,
    getRefreshToken,
    isAccessTokenExpired,
    isAuthenticated,
    getUserRoles,
    hasRole,
    getAuthenticatedUser,
    saveTokens,
    clearTokens,
    refreshAccessToken,
    login,
    logout,
    register,
    decodeJWT
  };
})();
