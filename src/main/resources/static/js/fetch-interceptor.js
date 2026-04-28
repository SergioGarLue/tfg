/**
 * Fetch Interceptor - Intercepta todas las peticiones fetch para agregar JWT
 * Reemplaza el fetch global para adjuntar automáticamente el token Authorization
 */

const originalFetch = window.fetch;

window.fetch = async function(...args) {
  const [resource, config = {}] = args;

  // Agregar headers si no existen
  if (!config.headers) {
    config.headers = {};
  }

  // Si hay un token válido, agregarlo al header Authorization
  // No agregar si es un endpoint de autenticación (login/register/refresh)
  const token = AUTH.getAccessToken();
  const isAuthEndpoint = typeof resource === 'string' && resource.includes('/api/auth/');
  if (token && !isAuthEndpoint && !config.headers['Authorization']) {
    config.headers['Authorization'] = `Bearer ${token}`;
  }

  // Asegurar que tenemos Content-Type para peticiones con body
  if (config.body && !config.headers['Content-Type'] && !(config.body instanceof FormData)) {
    config.headers['Content-Type'] = 'application/json';
  }

  try {
    let response = await originalFetch.call(this, resource, config);

    // Si obtenemos 401 (no autorizado), intentar renovar el token
    // Pero no para el endpoint de refresh para evitar bucles
    if (response.status === 401 && !resource.includes('/api/auth/refresh')) {
      console.warn('Token expirado, intentando renovar...');
      
      const refreshed = await AUTH.refreshAccessToken();
      if (refreshed) {
        // Reintentar la petición con el nuevo token
        const newToken = AUTH.getAccessToken();
        if (newToken) {
          config.headers['Authorization'] = `Bearer ${newToken}`;
          response = await originalFetch.call(this, resource, config);
        }
      } else {
        // Si no se puede renovar, redirigir a login
        AUTH.logout();
        window.location.href = '/login';
      }
    }

    return response;
  } catch (error) {
    console.error('Error en fetch interceptor:', error);
    throw error;
  }
};
