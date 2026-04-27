async function loadSidebar() {
  const container = document.getElementById('sidebar-container');
  if (!container) {
    console.error('Sidebar container not found');
    return;
  }

  try {
    const response = await fetch('/sidebar.html');
    if (!response.ok) {
      throw new Error(`HTTP ${response.status} - ${response.statusText}`);
    }
    const html = await response.text();
    container.innerHTML = html;
    initSidebarDropdowns();
    initSidebarMobileToggle();
    initSidebarIconButtons();
    renderSidebarUserState();
    document.dispatchEvent(new CustomEvent('sidebarLoaded'));
  } catch (error) {
    console.error('Error cargando sidebar.html:', error);
    container.innerHTML = '<div class="sidebar-error">No se pudo cargar el sidebar. Vuelve a intentarlo.</div>';
  }
}

async function cargarImagenEldenRing() {
  try {
    const response = await fetch('JSON/steam_top_1000_sellers.json');
    if (!response.ok) throw new Error(`Error ${response.status}: ${response.statusText}`);
    const juegos = await response.json();

    console.log('JSON cargado. Total juegos:', juegos.length);

    const eldenRing = juegos.find(juego => juego.name === 'ELDEN RING');
    console.log('Elden Ring encontrado:', eldenRing);

    if (eldenRing && eldenRing.header_image) {
      const heroSection = document.getElementById('hero-elden-ring');
      console.log('Hero section encontrada:', heroSection);
      if (heroSection) {
        const bgUrl = eldenRing.header_image;
        console.log('Aplicando imagen:', bgUrl);
        heroSection.style.backgroundImage = `linear-gradient(135deg, rgba(27,40,56,0.3) 0%, rgba(10,14,39,0.4) 100%), url('${bgUrl}')`;
        heroSection.style.backgroundSize = 'cover';
        heroSection.style.backgroundPosition = 'center';
        heroSection.style.backgroundAttachment = 'fixed';

        const btnHero = heroSection.querySelector('.btn-hero');
        if (btnHero && eldenRing.appid) {
          btnHero.onclick = () => {
            window.location.href = `/juego/${eldenRing.appid}`;
          };
        }
      }
    } else {
      console.warn('Elden Ring no encontrado en el JSON o sin header_image');
    }
  } catch (error) {
    console.error('Error al cargar la imagen de Elden Ring:', error);
  }
}

// Funciones de autenticación integradas con JWT
const getUsuarioLogueado = () => {
  if (typeof AUTH !== 'undefined' && AUTH && typeof AUTH.getAuthenticatedUser === 'function') {
    return AUTH.getAuthenticatedUser();
  }
  return null;
};

const saveUsuarioLogueado = (usuario) => {
  if (typeof AUTH !== 'undefined' && AUTH && typeof AUTH.saveTokens === 'function') {
    // Para actualizar solo el usuario, necesitamos los tokens actuales
    const accessToken = AUTH.getAccessToken();
    const refreshToken = AUTH.getRefreshToken();
    AUTH.saveTokens(accessToken, refreshToken, usuario);
  } else {
    // Fallback: guardar directamente en localStorage
    localStorage.setItem('user', JSON.stringify(usuario));
  }
};

const logoutUsuario = () => {
  if (typeof AUTH !== 'undefined' && AUTH && typeof AUTH.logout === 'function') {
    AUTH.logout();
  }
  window.location.href = '/login?logout=true';
};

const DEFAULT_AVATAR = 'https://upload.wikimedia.org/wikipedia/commons/thumb/2/2c/Default_pfp.svg/500px-Default_pfp.svg.png';
const getAvatarUrl = (usuario) => {
  if (usuario?.imagenUsuario) {
    return usuario.imagenUsuario;
  }
  if (usuario?.imagen) {
    return usuario.imagen;
  }
  return DEFAULT_AVATAR;
};

const renderSidebarUserState = async () => {
  const usuario = getUsuarioLogueado();
  const avatar = document.getElementById('sidebar-avatar');
  const username = document.getElementById('sidebar-username');
  const profileLink = document.getElementById('sidebar-profile-link');
  const authButton = document.getElementById('sidebar-auth-button');
  const adminLink = document.getElementById('admin-link');
  const adminSection = document.getElementById('admin-section');
  const isAdmin = typeof AUTH !== 'undefined' && AUTH && typeof AUTH.hasRole === 'function' && AUTH.hasRole('ROLE_ADMIN');

  if (avatar) {
    avatar.src = getAvatarUrl(usuario);
    avatar.alt = usuario?.nombreUsuario || 'Invitado';
  }

  if (username) {
    username.textContent = usuario?.nombreUsuario || 'Invitado';
  }

  if (profileLink) {
    profileLink.href = usuario ? '/perfil' : '/login';
  }

  // Mostrar/ocultar enlace admin según rol
  if (adminSection) {
    if (usuario && isAdmin) {
      adminSection.style.display = 'block';
      if (adminLink) {
        adminLink.href = '/admin';
      }
    } else {
      adminSection.style.display = 'none';
    }
  }

  if (authButton) {
    authButton.removeEventListener('click', logoutUsuario);
    if (usuario) {
      authButton.textContent = 'Cerrar sesión';
      authButton.href = '#';
      authButton.addEventListener('click', (event) => {
        event.preventDefault();
        logoutUsuario();
      });
    } else {
      authButton.textContent = 'Iniciar sesión';
      authButton.href = '/login';
    }
  }
};

const renderProfileUsuario = async () => {
  const urlParams = new URLSearchParams(window.location.search);
  const profileId = urlParams.get('id');
  const currentUser = getUsuarioLogueado();
  const currentUserId = currentUser ? parseInt(currentUser.idUsuario, 10) : null;
  const profileOwnerId = profileId ? parseInt(profileId, 10) : currentUserId;
  
  let usuario = currentUser;
  
  // If viewing another user's profile
  if (profileId && (!currentUserId || parseInt(profileId, 10) !== currentUserId)) {
    try {
      const response = await fetch(`/api/usuarios/${profileId}`, {
        headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
      });
      if (response.ok) {
        usuario = await response.json();
        document.body.classList.add('perfil-externo');
        document.title = `${usuario.nombreUsuario} - Perfil - Solar Sistem`;
      } else {
        console.error('Error cargando perfil externo:', response.status);
      }
    } catch (error) {
      console.error('Error cargando perfil externo:', error);
    }
  }
  
  const avatarPreview = document.getElementById('avatar-preview');
  const nombreUsuario = document.getElementById('perfil-nombre-usuario');
  const correoUsuario = document.getElementById('perfil-correo-usuario');

  if (avatarPreview) {
    avatarPreview.src = getAvatarUrl(usuario);
    avatarPreview.alt = usuario?.nombreUsuario || 'Usuario';
  }
  if (nombreUsuario) {
    nombreUsuario.textContent = usuario?.nombreUsuario || 'Usuario';
  }
  if (correoUsuario) {
    correoUsuario.textContent = usuario?.correoElectronico || 'usuario@ejemplo.com';
  }

  const perfilPais = document.getElementById('perfil-pais');
  const perfilBiografia = document.getElementById('perfil-biografia');
  const perfilJuegosCount = document.getElementById('perfil-juegos-count');
  const perfilAmigosCount = document.getElementById('perfil-amigos-count');
  const perfilHorasCount = document.getElementById('perfil-horas-count');
  const logrosGrid = document.getElementById('logros-grid');
  const logrosEmpty = document.getElementById('logros-empty');
  let horasJugadasCalculadas = 0;

  const renderAchievements = ({ juegos = 0, amigos = 0, horas = 0 } = {}) => {
    if (!logrosGrid) return;

    if (juegos === 0) {
      logrosGrid.innerHTML = '';
      if (logrosEmpty) {
        logrosEmpty.style.display = 'block';
      }
      return;
    }

    const achievements = [];
    achievements.push({ icon: 'fa-solid fa-trophy', label: 'Primer juego completado' });
    if (juegos >= 5) {
      achievements.push({ icon: 'fa-solid fa-layer-group', label: '5 Juegos en biblioteca' });
    }
    if (juegos >= 10) {
      achievements.push({ icon: 'fa-solid fa-star', label: '10 Juegos en biblioteca' });
    }
    if (amigos >= 10) {
      achievements.push({ icon: 'fa-solid fa-user-group', label: '10 Amigos' });
    }
    if (horas >= 50) {
      achievements.push({ icon: 'fa-solid fa-gamepad', label: '50 Horas de juego' });
    }
    if (achievements.length === 0) {
      achievements.push({ icon: 'fa-solid fa-gamepad', label: 'Has empezado tu colección' });
    }

    if (logrosEmpty) {
      logrosEmpty.style.display = 'none';
    }
    logrosGrid.innerHTML = achievements.map((logro) => `
      <div class="logro">
        <i class="${logro.icon}"></i>
        <span>${logro.label}</span>
      </div>
    `).join('');
  };

  const aplicarDatosPerfil = (perfil) => {
    if (!perfil) return;
    if (avatarPreview && perfil.imagenUsuario) {
      avatarPreview.src = perfil.imagenUsuario;
    }
    const cabeceraPerfil = document.querySelector('.cabecera-perfil');
    if (cabeceraPerfil && perfil.imagenFondoPerfil) {
      cabeceraPerfil.style.backgroundImage = `linear-gradient(135deg, rgba(27,40,56,0.3) 0%, rgba(10,14,39,0.4) 100%), url('${perfil.imagenFondoPerfil}')`;
      cabeceraPerfil.style.backgroundSize = 'cover';
      cabeceraPerfil.style.backgroundPosition = 'center';
    }
    if (perfilPais) {
      perfilPais.textContent = perfil.pais || 'No especificado';
    }
    if (perfilBiografia) {
      perfilBiografia.textContent = perfil.biografia || 'Este usuario aún no ha completado su biografía.';
    }
    if (perfilJuegosCount && currentUserId) {
      try {
        const juegosTotal = perfil.juegosTotal ?? null;
        if (juegosTotal !== null) {
          perfilJuegosCount.textContent = juegosTotal;
        }
      } catch (error) {
        console.warn('No se pudo actualizar el conteo de juegos desde el perfil:', error);
      }
    }
  };

  const generarHorasJugadas = (min, max) => {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  };

  const actualizarHorasJugadas = async (usuarioId) => {
    if (!perfilHorasCount || !usuarioId) return 0;
    try {
      const response = await fetch(`/api/biblioteca/usuario/${usuarioId}/cantidad`, {
        headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
      });
      if (!response.ok) {
        perfilHorasCount.textContent = '0';
        horasJugadasCalculadas = 0;
        return 0;
      }

      const cantidad = await response.json();
      if (Number(cantidad) > 0) {
        const horas = generarHorasJugadas(15, 150);
        perfilHorasCount.textContent = `${horas}`;
        horasJugadasCalculadas = horas;
        return horas;
      }
      perfilHorasCount.textContent = '0';
      horasJugadasCalculadas = 0;
      return 0;
    } catch (error) {
      console.warn('Error calculando horas jugadas:', error);
      perfilHorasCount.textContent = '0';
      horasJugadasCalculadas = 0;
      return 0;
    }
  };

  const actualizarContadoresPerfil = async () => {
    let juegosCount = 0;
    let amigosCount = 0;
    let horasCount = 0;

    if (perfilJuegosCount && usuario?.idUsuario) {
      try {
        const response = await fetch(`/api/biblioteca/usuario/${usuario.idUsuario}/cantidad`, {
          headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
        });
        if (response.ok) {
          const cantidad = await response.json();
          juegosCount = Number(cantidad) || 0;
          perfilJuegosCount.textContent = juegosCount;
        }
      } catch (error) {
        console.warn('Error calculando juegos de perfil:', error);
      }
    }

    if (perfilAmigosCount && usuario?.idUsuario) {
      const amigosAceptados = await fetchAcceptedFriendList(usuario.idUsuario);
      amigosCount = amigosAceptados.length;
      perfilAmigosCount.textContent = amigosCount;
    }

    horasCount = await actualizarHorasJugadas(profileOwnerId);
    renderAchievements({ juegos: juegosCount, amigos: amigosCount, horas: horasCount });
  };

  const perfilInfo = usuario?.perfilUsuario;
  if (perfilInfo) {
    aplicarDatosPerfil(perfilInfo);
  }

  if (currentUserId && (!profileId || parseInt(profileId, 10) === currentUserId)) {
    try {
      const response = await fetch('/api/perfil', {
        headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
      });
      if (response.ok) {
        const perfilData = await response.json();
        aplicarDatosPerfil(perfilData);
      }
    } catch (error) {
      console.warn('Error cargando datos dinámicos de perfil:', error);
    }
  }

  await renderRecentGames(profileOwnerId);
  actualizarContadoresPerfil();

  // Update section headings when viewing another user's profile
  if (document.body.classList.contains('perfil-externo')) {
    const actividadHeader = document.querySelector('#actividad h3');
    if (actividadHeader) actividadHeader.textContent = 'Actividad reciente';

    const logrosHeader = document.querySelector('#logros h3');
    if (logrosHeader) logrosHeader.textContent = 'Logros';
  }
};



const initSidebarDropdowns = () => {
  const sidebarItems = document.querySelectorAll('.item-desplegable');
  sidebarItems.forEach((item) => {
    const cabecera = item.querySelector('.cabecera-desplegable');
    const menu = item.querySelector('.contenedor-sublista');
    const flecha = item.querySelector('.flecha-dropdown');

    if (!cabecera || !menu || !flecha) return;

    cabecera.addEventListener('click', (e) => {
      e.stopPropagation();
      menu.classList.toggle('cerrado');
      flecha.classList.toggle('activo');
      const estaAbierto = !menu.classList.contains('cerrado');
      const subitems = menu.querySelectorAll('li');
      subitems.forEach((subitem, index) => {
        subitem.style.transitionDelay = estaAbierto ? `${index * 0.1}s` : '0s';
      });
    });
  });
};

const initSidebarMobileToggle = () => {
  const sidebar = document.querySelector('.barra-lateral');
  const toggle = document.querySelector('.sidebar-toggle');
  if (!sidebar || !toggle) return;

  toggle.addEventListener('click', () => {
    const expanded = sidebar.classList.toggle('sidebar-open');
    toggle.setAttribute('aria-expanded', expanded);
  });
};

const initSidebarIconButtons = () => {
  const buttons = document.querySelectorAll('.sidebar-icon[data-href]');
  buttons.forEach((button) => {
    const href = button.getAttribute('data-href');
    if (!href) return;

    button.addEventListener('click', () => {
      window.location.href = href;
    });
  });
};

const CARRITO_KEY = 'solarSistemCart';

const getCartCount = () => {
  const cart = JSON.parse(localStorage.getItem(CARRITO_KEY)) || [];
  return cart.reduce((sum, item) => sum + (Number(item.quantity) || 1), 0);
};

const actualizarContadorCarrito = (cantidad) => {
  const badge = document.getElementById('badge-carrito');
  if (badge) {
    if (cantidad > 0) {
      badge.style.display = 'flex';
      badge.textContent = cantidad;
    } else {
      badge.style.display = 'none';
    }
  }
};

window.actualizarContadorCarrito = actualizarContadorCarrito;
window.getCartCount = getCartCount;

window.addEventListener('storage', (event) => {
  if (event.key === CARRITO_KEY) {
    actualizarContadorCarrito(getCartCount());
  }
});

const buildNotificationHTML = ({ icon, title, time, unread, url }) => `
  <div class="notificacion ${unread ? 'no-leida' : ''}" ${url ? `data-url="${url}"` : ''}>
    <div class="icono-notificacion"><i class="fa-solid ${icon}"></i></div>
    <div class="contenido-notificacion">
      <p class="texto-notificacion">${title}</p>
      <span class="tiempo-notificacion">${time}</span>
    </div>
`;

const getFriendList = () => {
  const storedFriends = JSON.parse(localStorage.getItem('listaAmigos')) || [];
  return Array.isArray(storedFriends) ? storedFriends : [];
};

const getCurrentUserId = () => {
  const usuario = getUsuarioLogueado();
  return usuario?.idUsuario ? parseInt(usuario.idUsuario, 10) : null;
};

const fetchAcceptedFriendList = async (usuarioId) => {
  if (!usuarioId) return [];
  try {
    const response = await fetch(`/api/amistades/usuario/${usuarioId}/aceptadas`, {
      headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
    });
    if (!response.ok || response.status === 204) return [];
    return await response.json();
  } catch (error) {
    console.warn('No se pudo cargar lista de amigos del servidor:', error);
    return [];
  }
};

const getFriendName = (friend) => {
  if (typeof friend === 'string') return friend;
  if (typeof friend === 'object' && friend !== null) {
    return friend.nombre || friend.name || friend.nombreUsuario || friend.username || friend.usuario || 'Amigo';
  }
  return 'Amigo';
};

const fetchRecentPurchasedGames = async (usuarioId) => {
  if (!usuarioId) return [];
  try {
    const response = await fetch(`/api/biblioteca/usuario/${usuarioId}/recientes`, {
      headers: { 'Authorization': `Bearer ${AUTH.getAccessToken()}` }
    });
    if (!response.ok || response.status === 204) return [];
    return await response.json();
  } catch (error) {
    console.warn('Error cargando compras recientes:', error);
    return [];
  }
};

const formatPurchaseDate = (fechaStr) => {
  if (!fechaStr) return '';
  const date = new Date(fechaStr);
  return date.toLocaleDateString('es-ES', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });
};

const renderRecentGames = async (usuarioId) => {
  const container = document.querySelector('.actividad-reciente');
  if (!container) return;

  const recientes = await fetchRecentPurchasedGames(usuarioId);
  if (!Array.isArray(recientes) || recientes.length === 0) {
    container.innerHTML = '';
    return;
  }

  container.innerHTML = recientes.map(item => {
    const juego = item?.juego || {};
    const titulo = juego.titulo || 'Juego reciente';
    const imagenUrl = juego.imagen || 'https://via.placeholder.com/100x60';
    const fecha = formatPurchaseDate(item.fechaAdquisicion);

    return `
      <div class="juego-actividad">
        <img src="${imagenUrl}" alt="${titulo}" onerror="this.onerror=null;this.src='https://via.placeholder.com/100x60';">
        <div class="info-actividad">
          <span class="titulo-juego">${titulo}</span>
          <span class="detalle-actividad">Comprado ${fecha}</span>
        </div>
      </div>
    `;
  }).join('');
};

const getFriendCollectionGames = (friend) => {
  if (!friend || typeof friend !== 'object') return [];
  const candidateCollections = [friend.coleccion, friend.coleccionJuegos, friend.games, friend.juegos, friend.collection];
  const collection = candidateCollections.find(Array.isArray);
  if (!Array.isArray(collection)) return [];
  return collection
    .map((item) => {
      if (!item) return null;
      if (typeof item === 'string') {
        return { name: item };
      }
      if (typeof item === 'object') {
        return item;
      }
      return null;
    })
    .filter(Boolean);
};

const getRandomItem = (items) => items[Math.floor(Math.random() * items.length)];

const loadDynamicNotificaciones = async () => {
  const listaNotificaciones = document.querySelector('.lista-notificaciones');
  if (!listaNotificaciones) return;

  const currentUserId = getCurrentUserId();
  let friends = currentUserId ? await fetchAcceptedFriendList(currentUserId) : [];
  if (!friends.length) {
    friends = getFriendList();
  }

  let juegos = [];
  try {
    const response = await fetch('/JSON/steam_top_1000_sellers.json');
    if (response.ok) {
      juegos = await response.json();
    }
  } catch (error) {
    console.warn('No se pudo cargar el JSON de juegos para notificaciones:', error);
  }

  const juegosConOferta = juegos.filter(juego => Number(juego.price?.discount_percent) > 0);
  const ofertaJuego = juegosConOferta.length ? getRandomItem(juegosConOferta) : getRandomItem(juegos);
  const ofertaTitulo = ofertaJuego
    ? `¡Oferta especial! ${ofertaJuego.name || ofertaJuego.titulo || ofertaJuego.title} está ${Number(ofertaJuego.price?.discount_percent) || 0}% más barato`
    : '¡Hay una oferta nueva en la tienda!';
  const ofertaUrl = ofertaJuego?.appid ? `/juego/${ofertaJuego.appid}` : '';

  const notificaciones = [
    {
      icon: 'fa-tag',
      title: ofertaTitulo,
      time: 'Hace unos minutos',
      unread: true,
      url: ofertaUrl
    }
  ];

  if (friends.length) {
    const selectedFriends = friends.length > 1
      ? [getRandomItem(friends), getRandomItem(friends.filter((f) => f !== friends[0]))]
      : [friends[0]];

    const friendNotifications = selectedFriends.map((friend) => {
      const friendName = getFriendName(friend);
      const friendGames = getFriendCollectionGames(friend);
      const chosenGame = friendGames.length ? getRandomItem(friendGames) : getRandomItem(juegos);
      const gameTitle = chosenGame?.name || chosenGame?.titulo || chosenGame?.title || 'un juego';
      const gameUrl = chosenGame?.appid ? `/juego/${chosenGame.appid}` : '';

      return {
        icon: 'fa-user-check',
        title: `${friendName} ha añadido ${gameTitle} a su colección`,
        time: 'Hace poco',
        unread: true,
        url: gameUrl
      };
    });

    notificaciones.push(...friendNotifications);
  }

  listaNotificaciones.innerHTML = notificaciones.map(buildNotificationHTML).join('');
};

const setupNotificaciones = async () => {
  const botonNotificaciones = document.getElementById('boton-notificaciones');
  const menuNotificaciones = document.getElementById('menu-notificaciones');

  if (!botonNotificaciones || !menuNotificaciones) return;

  botonNotificaciones.addEventListener('click', (e) => {
    e.stopPropagation();
    menuNotificaciones.classList.toggle('activo');
  });

  document.addEventListener('click', (e) => {
    if (!menuNotificaciones.contains(e.target) && !botonNotificaciones.contains(e.target)) {
      menuNotificaciones.classList.remove('activo');
    }
  });

  const btnMarcarLeidas = menuNotificaciones.querySelector('.btn-marcar-leidas');
  const listaNotificaciones = menuNotificaciones.querySelector('.lista-notificaciones');

  if (btnMarcarLeidas) {
    btnMarcarLeidas.addEventListener('click', () => {
      const notificaciones = menuNotificaciones.querySelectorAll('.notificacion.no-leida');
      notificaciones.forEach(n => n.classList.remove('no-leida'));
    });
  }

  await loadDynamicNotificaciones();

  if (listaNotificaciones) {
    listaNotificaciones.addEventListener('click', (event) => {
      const notification = event.target.closest('.notificacion');
      if (!notification) return;
      const url = notification.dataset.url;
      if (url) {
        window.location.href = url;
      }
    });
  }
};

const fetchJuegos = async () => {
  try {
    const response = await fetch('/api/tienda');
    if (!response.ok) throw new Error('Error al obtener juegos');
    const juegos = await response.json();

    renderizarJuegos(juegos);
  } catch (error) {
    console.error('Hubo un problema con la petición fetch:', error);
  }
};

const renderizarJuegos = (juegos) => {
  const contenedorNovedades = document.getElementById('fila-novedades');
  const contenedorRecomendados = document.getElementById('fila-recomendados');
  const contenedorRecientes = document.getElementById('fila-recientes');

  if (!contenedorNovedades || !contenedorRecomendados || !contenedorRecientes) return;

  contenedorNovedades.innerHTML = '';
  contenedorRecomendados.innerHTML = '';
  contenedorRecientes.innerHTML = '';

  juegos.forEach((juego, index) => {
    const id = juego.idJuego || juego.appid || juego.id;
    const titulo = juego.titulo || juego.name || juego.title || 'Juego';
    const imagenUrl = juego.imagen || juego.header_image || juego.image || (id ? `https://cdn.akamai.steamstatic.com/steam/apps/${id}/header.jpg` : 'https://via.placeholder.com/300x200');
    let precioNum = juego.precio !== undefined ? juego.precio : (juego.price ? juego.price.final : 0);
    const precioTexto = precioNum === 0 ? 'Gratis' : `${precioNum}€`;
    const generosRaw = juego.generos || juego.genres;
    const etiquetas = Array.isArray(generosRaw)
      ? generosRaw.map(g => typeof g === 'object' ? g.nombre : g).join(', ')
      : 'Acción';

    const tarjetaHTML = `
      <a href="/juego/${id}" class="tarjeta">
        <img src="${imagenUrl}" alt="${titulo}" onerror="this.onerror=null;this.src='https://via.placeholder.com/300x200';">
        <div class="info-tarjeta">
          <span class="titulo-juego">${titulo}</span>
          <div class="etiquetas">${etiquetas}</div>
          <div class="precio-container">
            <span class="precio">${precioTexto}</span>
          </div>
        </div>
      </a>
    `;

    if (index < 7) {
      contenedorNovedades.innerHTML += tarjetaHTML;
    } else if (index < 14) {
      contenedorRecomendados.innerHTML += tarjetaHTML;
    } else if (index < 21) {
      contenedorRecientes.innerHTML += tarjetaHTML;
    }
  });
};

async function main() {
  // Ensure AUTH is available before user-dependent renders
  if (typeof AUTH === 'undefined') {
    console.warn('AUTH module not loaded, skipping user state renders');
  }
  await loadSidebar();
  await renderProfileUsuario();
  actualizarContadorCarrito(getCartCount());
  setupNotificaciones();
  fetchJuegos();
}


document.addEventListener('DOMContentLoaded', main);
