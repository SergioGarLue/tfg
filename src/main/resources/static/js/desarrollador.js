/**
 * Panel B2B para Desarrolladores
 * Gestion de precios, descuentos y disponibilidad de juegos
 */

const API_DESARROLLADOR = '/api/desarrollador';

let juegosCache = [];

document.addEventListener('DOMContentLoaded', () => {
  cargarMisJuegos();
});

async function cargarMisJuegos() {
  const tbody = document.getElementById('tabla-juegos-body');
  const panelVacio = document.getElementById('panel-vacio');

  try {
    const response = await fetch(`${API_DESARROLLADOR}/mis-juegos`, {
      headers: {
        'Authorization': `Bearer ${AUTH.getAccessToken()}`
      }
    });

    if (!response.ok) {
      if (response.status === 403) {
        tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#ff3b30;padding:24px;">Acceso denegado. No tienes permisos de desarrollador.</td></tr>';
        return;
      }
      throw new Error('Error al cargar juegos');
    }

    const juegos = await response.json();
    juegosCache = juegos || [];

    if (!juegos || juegos.length === 0) {
      tbody.innerHTML = '';
      panelVacio.style.display = 'block';
      actualizarEstadisticas([]);
      return;
    }

    panelVacio.style.display = 'none';
    renderizarTabla(juegos);
    actualizarEstadisticas(juegos);
  } catch (error) {
    console.error('Error cargando juegos:', error);
    tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#ff3b30;padding:24px;">Error al cargar los juegos. Intenta de nuevo.</td></tr>';
  }
}

function renderizarTabla(juegos) {
  const tbody = document.getElementById('tabla-juegos-body');
  tbody.innerHTML = juegos.map(juego => {
    const precioBase = juego.precio != null ? juego.precio.toFixed(2) : '0.00';
    const porcentaje = juego.porcentaje != null ? juego.porcentaje : 0;
    const precioRebajado = juego.precioRebajado != null ? juego.precioRebajado.toFixed(2) : '';
    const disponible = juego.disponible !== false;
    const esGratis = disponible && juego.precio === 0;

    let estadoValor = 'disponible';
    if (!disponible) estadoValor = 'no-disponible';
    else if (esGratis) estadoValor = 'gratis';

    return `
      <tr data-id="${juego.idJuego}">
        <td>
          <div class="celda-juego">
            <img src="${juego.imagen}" alt="${juego.titulo}" onerror="this.src='https://via.placeholder.com/48x32'">
            <span class="titulo">${juego.titulo}</span>
          </div>
        </td>
        <td>
          <input type="number" class="input-precio" value="${precioBase}" min="0" step="0.01" data-campo="precio">
        </td>
        <td>
          <input type="number" class="input-precio input-descuento" value="${porcentaje}" min="0" max="100" step="1" data-campo="porcentaje">
        </td>
        <td>
          <input type="number" class="input-precio" value="${precioRebajado}" min="0" step="0.01" data-campo="precioRebajado" placeholder="Opcional">
        </td>
        <td>
          <select class="select-disponible" data-campo="disponible">
            <option value="disponible" ${estadoValor === 'disponible' ? 'selected' : ''}>Disponible</option>
            <option value="gratis" ${estadoValor === 'gratis' ? 'selected' : ''}>Gratis</option>
          </select>
        </td>
        <td>
          <button class="btn-guardar" onclick="guardarCambios(${juego.idJuego}, this)">
            <i class="fa-solid fa-floppy-disk"></i> Guardar
          </button>
        </td>
      </tr>
    `;
  }).join('');

  // Activar calculadora de descuentos en cada fila
  juegos.forEach(juego => {
    const fila = tbody.querySelector(`tr[data-id="${juego.idJuego}"]`);
    if (fila) bindCalculadoraDescuento(fila);
  });
}

/**
 * Vincula los inputs de precio, porcentaje y precio rebajado para calcularse automáticamente.
 */
function bindCalculadoraDescuento(fila) {
  const precioInput = fila.querySelector('input[data-campo="precio"]');
  const porcentajeInput = fila.querySelector('input[data-campo="porcentaje"]');
  const rebajadoInput = fila.querySelector('input[data-campo="precioRebajado"]');
  const selectEstado = fila.querySelector('select[data-campo="disponible"]');

  if (!precioInput || !porcentajeInput || !rebajadoInput) return;

  // Al cambiar precio base: recalcular precio rebajado según porcentaje actual
  precioInput.addEventListener('input', () => {
    if (selectEstado.value === 'gratis') return;
    const precio = parseFloat(precioInput.value) || 0;
    const porcentaje = parseInt(porcentajeInput.value, 10);
    if (precio > 0 && porcentaje > 0) {
      const rebajado = precio * (1 - porcentaje / 100);
      rebajadoInput.value = rebajado > 0 ? rebajado.toFixed(2) : '';
    }
  });

  // Al cambiar porcentaje: calcular precio rebajado
  porcentajeInput.addEventListener('input', () => {
    if (selectEstado.value === 'gratis') return;
    const precio = parseFloat(precioInput.value) || 0;
    const porcentaje = parseInt(porcentajeInput.value, 10) || 0;
    if (precio > 0) {
      if (porcentaje === 0) {
        rebajadoInput.value = '';
      } else {
        const rebajado = precio * (1 - porcentaje / 100);
        rebajadoInput.value = rebajado > 0 ? rebajado.toFixed(2) : '';
      }
    }
  });

  // Al cambiar precio rebajado: calcular porcentaje
  rebajadoInput.addEventListener('input', () => {
    if (selectEstado.value === 'gratis') return;
    const precio = parseFloat(precioInput.value) || 0;
    const rebajado = parseFloat(rebajadoInput.value);
    if (precio > 0 && rebajado >= 0 && rebajado < precio) {
      const porcentaje = Math.round((1 - rebajado / precio) * 100);
      porcentajeInput.value = porcentaje;
    } else if (!rebajadoInput.value) {
      porcentajeInput.value = 0;
    }
  });
}

function actualizarEstadisticas(juegos) {
  const total = juegos.length;
  const activos = juegos.filter(j => j.disponible !== false).length;
  const descuentos = juegos.filter(j => j.porcentaje != null && j.porcentaje > 0).length;

  animarContador('stat-total', total);
  animarContador('stat-activos', activos);
  animarContador('stat-descuentos', descuentos);
}

function animarContador(id, valorFinal) {
  const el = document.getElementById(id);
  if (!el) return;
  const valorInicial = parseInt(el.textContent, 10) || 0;
  if (valorInicial === valorFinal) {
    el.textContent = valorFinal;
    return;
  }
  const duracion = 400;
  const inicio = performance.now();
  function tick(now) {
    const progreso = Math.min((now - inicio) / duracion, 1);
    const actual = Math.round(valorInicial + (valorFinal - valorInicial) * progreso);
    el.textContent = actual;
    if (progreso < 1) requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
}

function validarDTO(dto, fila) {
  const precioInput = fila.querySelector('input[data-campo="precio"]');
  const porcentajeInput = fila.querySelector('input[data-campo="porcentaje"]');
  const rebajadoInput = fila.querySelector('input[data-campo="precioRebajado"]');

  if (dto.precio != null && dto.precio < 0) {
    precioInput?.classList.add('input-error');
    return 'El precio base no puede ser negativo';
  }
  precioInput?.classList.remove('input-error');

  if (dto.porcentaje != null && (dto.porcentaje < 0 || dto.porcentaje > 100)) {
    porcentajeInput?.classList.add('input-error');
    return 'El descuento debe estar entre 0 y 100';
  }
  porcentajeInput?.classList.remove('input-error');

  if (dto.precioRebajado != null && dto.precio != null && dto.precioRebajado > dto.precio) {
    rebajadoInput?.classList.add('input-error');
    return 'El precio rebajado no puede ser mayor que el precio base';
  }
  rebajadoInput?.classList.remove('input-error');

  return null;
}

async function guardarCambios(idJuego, btn) {
  const fila = document.querySelector(`tr[data-id="${idJuego}"]`);
  if (!fila) return;

  const inputs = fila.querySelectorAll('input, select');
  const dto = {};
  let estadoSeleccionado = 'disponible';

  inputs.forEach(input => {
    const campo = input.dataset.campo;
    let valor = input.value;

    if (campo === 'disponible') {
      estadoSeleccionado = valor;
      if (valor === 'no-disponible') {
        dto.disponible = false;
      } else if (valor === 'gratis') {
        dto.disponible = true;
        dto.precio = 0.0;
        dto.precioRebajado = null;
        dto.porcentaje = null;
      } else {
        dto.disponible = true;
      }
    } else if (campo === 'precio' || campo === 'precioRebajado') {
      dto[campo] = valor ? parseFloat(valor) : null;
    } else if (campo === 'porcentaje') {
      dto[campo] = valor ? parseInt(valor, 10) : null;
    }
  });

  // Si es gratis, ignorar valores de inputs numéricos (ya fueron forzados arriba)
  if (estadoSeleccionado === 'gratis') {
    dto.precio = 0.0;
    dto.precioRebajado = null;
    dto.porcentaje = null;
  }

  const errorValidacion = validarDTO(dto, fila);
  if (errorValidacion) {
    mostrarToast(errorValidacion, 'error');
    return;
  }

  btn.disabled = true;
  btn.innerHTML = '<div class="spinner-carga"></div> Guardando...';

  try {
    const response = await fetch(`${API_DESARROLLADOR}/juego/${idJuego}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${AUTH.getAccessToken()}`
      },
      body: JSON.stringify(dto)
    });

    if (response.ok) {
      const juegoActualizado = await response.json();
      mostrarToast('Cambios guardados correctamente', 'exito');
      actualizarFilaVisual(fila, juegoActualizado);
      actualizarJuegoEnCache(juegoActualizado);
      actualizarEstadisticas(juegosCache);
    } else if (response.status === 403) {
      mostrarToast('No tienes permiso para modificar este juego', 'error');
    } else if (response.status === 400) {
      const msg = await response.text().catch(() => 'Solicitud incorrecta');
      mostrarToast(msg || 'Datos invalidos', 'error');
    } else {
      mostrarToast('Error al guardar los cambios', 'error');
    }
  } catch (error) {
    console.error('Error guardando cambios:', error);
    mostrarToast('Error de conexion', 'error');
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="fa-solid fa-floppy-disk"></i> Guardar';
  }
}

function actualizarFilaVisual(fila, juego) {
  const select = fila.querySelector('select[data-campo="disponible"]');
  const precioInput = fila.querySelector('input[data-campo="precio"]');
  const porcentajeInput = fila.querySelector('input[data-campo="porcentaje"]');
  const rebajadoInput = fila.querySelector('input[data-campo="precioRebajado"]');

  if (juego.disponible === false) {
    select.value = 'no-disponible';
  } else if (juego.precio === 0) {
    select.value = 'gratis';
  } else {
    select.value = 'disponible';
  }

  if (juego.precio != null) {
    precioInput.value = juego.precio.toFixed(2);
  }
  if (porcentajeInput) {
    porcentajeInput.value = juego.porcentaje != null ? juego.porcentaje : 0;
  }
  if (rebajadoInput) {
    rebajadoInput.value = juego.precioRebajado != null ? juego.precioRebajado.toFixed(2) : '';
  }

  // Quitar posible estado de error
  fila.querySelectorAll('.input-error').forEach(el => el.classList.remove('input-error'));
}

function actualizarJuegoEnCache(juegoActualizado) {
  const idx = juegosCache.findIndex(j => j.idJuego === juegoActualizado.idJuego);
  if (idx !== -1) {
    juegosCache[idx] = juegoActualizado;
  }
}

function mostrarToast(mensaje, tipo) {
  const toast = document.getElementById('toast-b2b');
  toast.textContent = mensaje;
  toast.className = `toast-b2b ${tipo}`;

  requestAnimationFrame(() => {
    toast.classList.add('visible');
  });

  setTimeout(() => {
    toast.classList.remove('visible');
  }, 3000);
}

