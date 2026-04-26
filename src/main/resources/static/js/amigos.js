/**
 * amigos.js - Gestión de amistades y solicitudes pendientes
 * Conecta con /api/amistades via fetch + JWT
 */

const API_BASE = '/api';
let usuarioActualId = null;

function obtenerIdUsuarioActual() {
    // Preferimos el objeto usuario guardado en localStorage por el login
    const user = AUTH.getAuthenticatedUser();
    if (user && user.idUsuario) {
        return parseInt(user.idUsuario, 10);
    }
    // Fallback: intentar leer del JWT (aunque sub suele ser username, no ID numérico)
    const token = AUTH.getAccessToken();
    if (!token) return null;
    const decoded = AUTH.decodeJWT(token);
    // Algunos JWT pueden tener el ID en un claim personalizado
    const id = decoded?.idUsuario || decoded?.userId || decoded?.sub;
    return id ? parseInt(id, 10) : null;
}

async function inicializarAmigos() {
    usuarioActualId = obtenerIdUsuarioActual();
    if (!usuarioActualId) {
        console.error('No se pudo obtener el ID del usuario actual');
        return;
    }

    configurarEventListeners();
    await cargarAmigosAceptados();
    await cargarSolicitudesPendientes();
    await actualizarBadgeAmistades();
}

function configurarEventListeners() {
    const btnBuscar = document.querySelector('.btn-buscar');
    const inputBuscar = document.querySelector('.buscar-amigos input');

    if (btnBuscar) {
        btnBuscar.addEventListener('click', buscarUsuarios);
    }
    if (inputBuscar) {
        inputBuscar.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') buscarUsuarios();
        });
    }
}

/* ─────────────── CARGAR AMIGOS ACEPTADOS ─────────────── */

async function cargarAmigosAceptados() {
    try {
        const response = await fetch(`${API_BASE}/amistades/usuario/${usuarioActualId}/aceptadas`, {
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        const container = document.querySelector('.seccion-tarjetas-amigos');
        if (!container) return;

        // Limpiar contenido estático previo
        container.innerHTML = '';

        if (response.status === 204) {
            container.innerHTML = '<p class="sin-amigos">No tienes amigos todavía. ¡Busca usuarios y envía solicitudes!</p>';
            return;
        }

        if (!response.ok) throw new Error('Error cargando amigos');

        const amigos = await response.json();
        mostrarAmigosAceptados(amigos);
    } catch (error) {
        console.error('Error cargando amigos:', error);
        container.innerHTML = '<p class="sin-amigos">Error al cargar amigos. Intenta recargar la página.</p>';
    }

}

function mostrarAmigosAceptados(amigos) {
    const container = document.querySelector('.seccion-tarjetas-amigos');
    if (!container) return;

    container.innerHTML = '';

    // Agrupar por estado de conexión
    const online = amigos.filter(a => a.conexion === 'ACTIVO');
    const away = amigos.filter(a => a.conexion === 'AUSENTE');
    const offline = amigos.filter(a => a.conexion === 'DESCONECTADO' || !a.conexion);


    if (online.length > 0) {
        container.appendChild(crearGrupoAmigos('En línea', online, 'estado-online'));
    }
    if (away.length > 0) {
        container.appendChild(crearGrupoAmigos('Ausentes', away, 'estado-away'));
    }
    if (offline.length > 0) {
        container.appendChild(crearGrupoAmigos('Desconectados', offline, 'estado-offline'));
    }

    if (amigos.length === 0) {
        container.innerHTML = '<p class="sin-amigos">No tienes amigos todavía. ¡Busca usuarios y envía solicitudes!</p>';
    }
}



function crearGrupoAmigos(titulo, amigos, claseEstado) {
    const grupo = document.createElement('div');
    grupo.className = 'grupo-amigos';

    const h3 = document.createElement('h3');
    h3.textContent = `${titulo} (${amigos.length})`;
    grupo.appendChild(h3);

    const lista = document.createElement('div');
    lista.className = 'lista-amigos';

    amigos.forEach(amigo => {
        const tarjeta = document.createElement('div');
        tarjeta.className = 'tarjeta-amigo';
        tarjeta.dataset.id = amigo.idUsuario;
        tarjeta.innerHTML = `
            <div class="avatar-amigo">
                <img src="${getAvatarUrl(amigo)}" alt="${amigo.nombreUsuario}">
                <div class="${claseEstado}"></div>
            </div>
            <div class="info-amigo">
                <span class="nombre-amigo">${amigo.nombreUsuario}</span>
                <span class="estado-amigo">${formatearEstado(amigo.conexion)}</span>
            </div>
            <div class="acciones-amigo">
                <button class="btn-accion-amigo" title="Enviar mensaje" onclick="enviarMensaje(${amigo.idUsuario})">
                    <i class="fa-solid fa-message"></i>
                </button>
                <button class="btn-accion-amigo" title="Ver perfil" onclick="verPerfil(${amigo.idUsuario})">
                    <i class="fa-solid fa-user"></i>
                </button>
                <button class="btn-accion-amigo btn-eliminar-amigo" title="Eliminar amigo" onclick="eliminarAmistad(${amigo.idUsuario})">
                    <i class="fa-solid fa-user-xmark"></i>
                </button>
            </div>
        `;
        lista.appendChild(tarjeta);
    });

    grupo.appendChild(lista);
    return grupo;
}

/* ─────────────── SOLICITUDES PENDIENTES ─────────────── */

async function cargarSolicitudesPendientes() {
    try {
        const response = await fetch(`${API_BASE}/amistades/usuario/${usuarioActualId}/pendientes`, {
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        const container = document.getElementById('contenedor-solicitudes');
        if (!container) return;

        if (response.status === 204) {
            container.innerHTML = '<p class="sin-solicitudes">No tienes solicitudes pendientes</p>';
            return;
        }

        if (!response.ok) throw new Error('Error cargando solicitudes');

        const solicitudes = await response.json();
        mostrarSolicitudesPendientes(solicitudes);
    } catch (error) {
        console.error('Error cargando solicitudes:', error);
    }
}

function mostrarSolicitudesPendientes(solicitudes) {
    const container = document.getElementById('contenedor-solicitudes');
    if (!container) return;

    container.innerHTML = '';

    if (solicitudes.length === 0) {
        container.innerHTML = '<p class="sin-solicitudes">No tienes solicitudes pendientes</p>';
        return;
    }

    solicitudes.forEach(sol => {
        const tarjeta = document.createElement('div');
        tarjeta.className = 'tarjeta-solicitud';
        tarjeta.dataset.id = sol.idAmistad;
        tarjeta.innerHTML = `
            <div class="info-solicitud">
                <img src="${getAvatarUrl(sol.solicitante)}" alt="${sol.solicitante.nombreUsuario}">
                <div>
                    <p><strong>${sol.solicitante.nombreUsuario}</strong> te envió una solicitud de amistad</p>
                    <small>${formatearFecha(sol.fechaPeticion)}</small>
                </div>
            </div>
            <div class="acciones-solicitud">
                <button class="btn-aceptar" onclick="aceptarSolicitud(${sol.idAmistad})">Aceptar</button>
                <button class="btn-rechazar" onclick="rechazarSolicitud(${sol.idAmistad})">Rechazar</button>
            </div>
        `;
        container.appendChild(tarjeta);
    });
}

/* ─────────────── ACCIONES ─────────────── */

async function aceptarSolicitud(idAmistad) {
    try {
        const response = await fetch(`${API_BASE}/amistades/aceptar/${idAmistad}?destinatarioId=${usuarioActualId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        if (response.ok) {
            mostrarToast('Solicitud aceptada', 'success');
            window.location.reload();
        } else {
            const msg = await response.text();
            mostrarToast(msg || 'Error al aceptar', 'error');
        }
    } catch (error) {
        console.error('Error aceptando solicitud:', error);
        mostrarToast('Error de conexión', 'error');
    }
}

async function rechazarSolicitud(idAmistad) {
    try {
        const response = await fetch(`${API_BASE}/amistades/rechazar/${idAmistad}?destinatarioId=${usuarioActualId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        if (response.ok) {
            mostrarToast('Solicitud rechazada', 'info');
            await cargarSolicitudesPendientes();
            await actualizarBadgeAmistades();
        } else {
            const msg = await response.text();
            mostrarToast(msg || 'Error al rechazar', 'error');
        }
    } catch (error) {
        console.error('Error rechazando solicitud:', error);
        mostrarToast('Error de conexión', 'error');
    }
}

async function eliminarAmistad(idAmigo) {
    if (!confirm('¿Estás seguro de que quieres eliminar este amigo?')) return;

    try {
        // Buscar la amistad correspondiente para obtener el idAmistad
        const responseAll = await fetch(`${API_BASE}/amistades`, {
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });
        const todas = await responseAll.json();
        const amistad = todas.find(a =>
            a.estado === 'ACEPTADO' &&
            (a.solicitante.idUsuario === usuarioActualId && a.destinatario.idUsuario === idAmigo) ||
            (a.destinatario.idUsuario === usuarioActualId && a.solicitante.idUsuario === idAmigo)
        );

        if (!amistad) {
            mostrarToast('No se encontró la amistad', 'error');
            return;
        }

        const response = await fetch(`${API_BASE}/amistades/${amistad.idAmistad}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        if (response.ok) {
            mostrarToast('Amigo eliminado', 'info');
            await cargarAmigosAceptados();
        } else {
            mostrarToast('Error al eliminar', 'error');
        }
    } catch (error) {
        console.error('Error eliminando amistad:', error);
        mostrarToast('Error de conexión', 'error');
    }
}

async function enviarSolicitudAmistad(destinatarioId) {
    try {
        const response = await fetch(`${API_BASE}/amistades/enviar?solicitanteId=${usuarioActualId}&destinatarioId=${destinatarioId}`, {
            method: 'POST',
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        if (response.ok) {
            mostrarToast('Solicitud enviada', 'success');
        } else {
            const msg = await response.text();
            mostrarToast(msg || 'Error al enviar solicitud', 'error');
        }
    } catch (error) {
        console.error('Error enviando solicitud:', error);
        mostrarToast('Error de conexión', 'error');
    }
}

/* ─────────────── BÚSQUEDA DE USUARIOS ─────────────── */

async function buscarUsuarios() {
    const input = document.querySelector('.buscar-amigos input');
    const query = input?.value.trim();
    if (!query) return;

    try {
        // Usar el endpoint existente de usuarios si lo hay, o listar todos y filtrar
        const response = await fetch(`${API_BASE}/usuarios`, {
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        if (!response.ok) throw new Error('Error buscando usuarios');

        const usuarios = await response.json();
        const filtrados = usuarios.filter(u =>
            u.idUsuario !== usuarioActualId &&
            u.nombreUsuario.toLowerCase().includes(query.toLowerCase())
        );

        mostrarResultadosBusqueda(filtrados);
    } catch (error) {
        console.error('Error buscando usuarios:', error);
        mostrarToast('Error al buscar usuarios', 'error');
    }
}

function mostrarResultadosBusqueda(usuarios) {
    let container = document.getElementById('resultados-busqueda');
    if (!container) {
        container = document.createElement('div');
        container.id = 'resultados-busqueda';
        container.className = 'resultados-busqueda';
        document.querySelector('.seccion-amigos')?.appendChild(container);
    }

    container.innerHTML = '<h3>Resultados de búsqueda</h3>';

    if (usuarios.length === 0) {
        container.innerHTML += '<p>No se encontraron usuarios</p>';
        return;
    }

    const lista = document.createElement('div');
    lista.className = 'lista-amigos';

    usuarios.forEach(u => {
        const tarjeta = document.createElement('div');
        tarjeta.className = 'tarjeta-amigo';
        tarjeta.innerHTML = `
            <div class="avatar-amigo">
                <img src="${getAvatarUrl(u)}" alt="${u.nombreUsuario}">
            </div>
            <div class="info-amigo">
                <span class="nombre-amigo">${u.nombreUsuario}</span>
            </div>
            <div class="acciones-amigo">
                <button class="btn-agregar-amigo" onclick="enviarSolicitudAmistad(${u.idUsuario})">
                    <i class="fa-solid fa-user-plus"></i> Agregar
                </button>
            </div>

        `;
        lista.appendChild(tarjeta);
    });

    container.appendChild(lista);
}

/* ─────────────── BADGE EN SIDEBAR ─────────────── */

async function actualizarBadgeAmistades() {
    try {
        const response = await fetch(`${API_BASE}/amistades/usuario/${usuarioActualId}/pendientes`, {
            headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });

        let cantidad = 0;
        if (response.ok && response.status !== 204) {
            const solicitudes = await response.json();
            cantidad = solicitudes.length;
        }

        const badge = document.getElementById('badge-amistades');
        if (badge) {
            badge.textContent = cantidad > 0 ? cantidad : '';
            badge.style.display = cantidad > 0 ? 'inline-block' : 'none';
        }
    } catch (error) {
        console.error('Error actualizando badge:', error);
    }
}

/* ─────────────── UTILIDADES ─────────────── */

function formatearEstado(estado) {
    const map = {
        'ACTIVO': 'En línea',
        'AUSENTE': 'Ausente',
        'DESCONECTADO': 'Desconectado',
        'JUGANDO': 'Jugando'
    };
    return map[estado] || estado || 'Desconectado';
}


function formatearFecha(fechaStr) {
    if (!fechaStr) return '';
    const date = new Date(fechaStr);
    return date.toLocaleString('es-ES', {
        day: '2-digit', month: 'short', year: 'numeric',
        hour: '2-digit', minute: '2-digit'
    });
}

function mostrarToast(mensaje, tipo = 'info') {
    // Crear toast inline para evitar recursión con window.mostrarToast
    const toast = document.createElement('div');
    toast.textContent = mensaje;
    toast.style.cssText = `
        position: fixed;
        bottom: 20px;
        right: 20px;
        padding: 12px 24px;
        border-radius: 4px;
        color: white;
        font-weight: 500;
        z-index: 9999;
        transition: opacity 0.3s;
        max-width: 300px;
        word-wrap: break-word;
    `;
    
    const colores = {
        success: '#4c6b22',
        error: '#c0392b',
        info: '#2a75b3',
        warning: '#f39c12'
    };
    toast.style.backgroundColor = colores[tipo] || colores.info;
    
    document.body.appendChild(toast);
    
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

function enviarMensaje(idUsuario) {
    mostrarToast('Función de mensajes no implementada aún', 'info');
}

function verPerfil(idUsuario) {
    window.location.href = `/perfil?id=${idUsuario}`;
}

/* ─────────────── INICIALIZACIÓN ─────────────── */

document.addEventListener('DOMContentLoaded', inicializarAmigos);
