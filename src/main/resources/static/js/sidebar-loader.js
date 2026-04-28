/**
 * sidebar-loader.js - Carga el sidebar dinámicamente en todas las páginas
 */

document.addEventListener('DOMContentLoaded', async function() {
    const container = document.getElementById('sidebar-container');
    if (!container) return;

    try {
        const response = await fetch('/sidebar.html');
        if (!response.ok) throw new Error('No se pudo cargar el sidebar');
        
        const html = await response.text();
        container.innerHTML = html;

        // Inicializar funcionalidades del sidebar después de cargarlo
        inicializarSidebar();
    } catch (error) {
        console.error('Error cargando sidebar:', error);
    }
});

function inicializarSidebar() {
    // Toggle del sidebar en móvil
    const toggle = document.querySelector('.sidebar-toggle');
    const sidebar = document.querySelector('.barra-lateral');
    
    if (toggle && sidebar) {
        toggle.addEventListener('click', () => {
            const isExpanded = toggle.getAttribute('aria-expanded') === 'true';
            toggle.setAttribute('aria-expanded', !isExpanded);
            sidebar.classList.toggle('abierto');
        });
    }

    // Dropdowns del sidebar
    document.querySelectorAll('.cabecera-desplegable').forEach(header => {
        header.addEventListener('click', (e) => {
            // No toggle si se hizo click en un link
            if (e.target.tagName === 'A') return;
            
            const submenu = header.nextElementSibling;
            if (submenu && submenu.classList.contains('contenedor-sublista')) {
                submenu.classList.toggle('cerrado');
                const arrow = header.querySelector('.flecha-dropdown');
                if (arrow) arrow.classList.toggle('rotado');
            }
        });
    });

    // Navegación por iconos inferiores
    document.querySelectorAll('.iconos-inferiores button[data-href]').forEach(btn => {
        btn.addEventListener('click', () => {
            const href = btn.getAttribute('data-href');
            if (href) window.location.href = href;
        });
    });

    // Actualizar info del usuario en el sidebar
    actualizarSidebarUsuario();
}

function actualizarSidebarUsuario() {
    const user = AUTH.getAuthenticatedUser();
    const usernameEl = document.getElementById('sidebar-username');
    const avatarEl = document.getElementById('sidebar-avatar');
    const profileLink = document.getElementById('sidebar-profile-link');

    if (user) {
        if (usernameEl) usernameEl.textContent = user.nombreUsuario || user.username || 'Usuario';
        if (avatarEl) avatarEl.src = user.avatar || 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/500px-Default_pfp.svg.png';
        if (profileLink) profileLink.href = '/perfil';
        
        // Mostrar sección admin si aplica
        const roles = AUTH.getUserRoles();
        const adminSection = document.getElementById('admin-section');
        if (adminSection && roles.includes('ADMIN')) {
            adminSection.style.display = 'block';
        }
    }
}
