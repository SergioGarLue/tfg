/**
 * Route Protection - Protege rutas del cliente contra acceso no autorizado
 * Redirige automáticamente a usuarios no autenticados a la página de login
 */

const ROUTE_PROTECTION = (() => {
  // Rutas públicas (accesibles sin autenticación)
  const PUBLIC_ROUTES = [
    '/',
    '/login',
    '/registro',
    '/juego',
    '/juego/**',
    '/tienda',
    '/index.html',
    '/login.html',
    '/registro.html'
  ];

  // Rutas protegidas por rol (requieren un rol específico)
  const ROLE_PROTECTED_ROUTES = {
    '/admin': ['ROLE_ADMIN'],
    '/desarrollador': ['ROLE_DESARROLLADOR'],
    '/editor': ['ROLE_EDITOR']
  };

  /**
   * Obtiene la ruta actual sin query params
   */
  const getCurrentRoute = () => {
    return window.location.pathname;
  };

  /**
   * Verifica si una ruta es pública
   */
  const isPublicRoute = (route) => {
    return PUBLIC_ROUTES.includes(route) || route === '' || route === '/index.html';
  };

  /**
   * Verifica si una ruta está protegida por rol
   */
  const getRoleRequirementForRoute = (route) => {
    for (const [protectedRoute, requiredRoles] of Object.entries(ROLE_PROTECTED_ROUTES)) {
      if (route.startsWith(protectedRoute)) {
        return requiredRoles;
      }
    }
    return null;
  };

  /**
   * Verifica si el usuario actual tiene permiso para la ruta
   */
  const hasPermissionForRoute = (route) => {
    // Si es ruta pública, permitir
    if (isPublicRoute(route)) {
      return true;
    }

    // Si no está autenticado, no permitir
    if (!AUTH.isAuthenticated()) {
      return false;
    }

    // Verificar protección por rol
    const requiredRoles = getRoleRequirementForRoute(route);
    if (requiredRoles) {
      return requiredRoles.some(role => AUTH.hasRole(role));
    }

    // Por defecto, permitir si está autenticado
    return true;
  };

  /**
   * Realiza la protección de rutas
   * Se debe llamar en cada cambio de navegación o carga de página
   */
  const protectCurrentRoute = () => {
    const currentRoute = getCurrentRoute();

    // Si es ruta pública, no hacer nada
    if (isPublicRoute(currentRoute)) {
      return;
    }

    // Si no tiene permiso, redirigir
    if (!hasPermissionForRoute(currentRoute)) {
      if (AUTH.isAuthenticated()) {
        // Está autenticado pero sin permisos (401)
        console.warn('Acceso denegado a:', currentRoute);
        window.location.href = '/';
      } else {
        // No está autenticado (403)
        console.warn('Acceso no autenticado a:', currentRoute);
        window.location.href = '/login';
      }
    }
  };

  /**
   * Monitorea cambios de navegación (para SPAs)
   */
  const startProtectionMonitor = () => {
    // Ejecutar al cargar
    window.addEventListener('DOMContentLoaded', protectCurrentRoute);

    // Ejecutar al cambiar hash (SPAs con hash routing)
    window.addEventListener('hashchange', protectCurrentRoute);

    // Interceptar clicks en enlaces internos
    document.addEventListener('click', (e) => {
      const link = e.target.closest('a[href]');
      if (link && link.hostname === window.location.hostname) {
        const href = link.getAttribute('href');
        if (href && !href.startsWith('http')) {
          const route = href.startsWith('/') ? href : '/' + href;
          if (!hasPermissionForRoute(route)) {
            e.preventDefault();
            console.warn('Acceso denegado a:', route);
            if (!AUTH.isAuthenticated()) {
              window.location.href = '/login';
            }
          }
        }
      }
    });
  };

  // API pública
  return {
    getCurrentRoute,
    isPublicRoute,
    getRoleRequirementForRoute,
    hasPermissionForRoute,
    protectCurrentRoute,
    startProtectionMonitor
  };
})();

// Iniciar la protección automáticamente
document.addEventListener('DOMContentLoaded', () => {
  ROUTE_PROTECTION.startProtectionMonitor();
  ROUTE_PROTECTION.protectCurrentRoute();
});
