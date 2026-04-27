/**
 * Panel B2B para Desarrolladores
 * Gestión de precios, descuentos y disponibilidad de juegos
 */

const API_DESARROLLADOR = '/api/desarrollador';

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
            <option value="no-disponible" ${estadoValor === 'no-disponible' ? 'selected' : ''}>No disponible</option>
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
}

function actualizarEstadisticas(juegos) {
  const total = juegos.length;
  const activos = juegos.filter(j => j.disponible !== false).length;
  const descuentos = juegos.filter(j => j.porcentaje != null && j.porcentaje > 0).length;

  document.getElementById('stat-total').textContent = total;
  document.getElementById('stat-activos').textContent = activos;
  document.getElementById('stat-descuentos').textContent = descuentos;
}

async function guardarCambios(idJuego, btn) {
  const fila = document.querySelector(`tr[data-id="${idJuego}"]`);
  if (!fila) return;

  const inputs = fila.querySelectorAll('input, select');
  const dto = {};

  inputs.forEach(input => {
    const campo = input.dataset.campo;
    let valor = input.value;

    if (campo === 'disponible') {
      if (valor === 'no-disponible') {
        dto.disponible = false;
      } else if (valor === 'gratis') {
        dto.disponible = true;
        dto.precio = 0.0;
      } else {
        dto.disponible = true;
      }
    } else if (campo === 'precio' || campo === 'precioRebajado') {
      dto[campo] = valor ? parseFloat(valor) : null;
    } else if (campo === 'porcentaje') {
      dto[campo] = valor ? parseInt(valor, 10) : null;
    }
  });

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
      mostrarToast('Cambios guardados correctamente', 'exito');
      const juegoActualizado = await response.json();
      actualizarFilaVisual(fila, juegoActualizado);
    } else if (response.status === 403) {
      mostrarToast('No tienes permiso para modificar este juego', 'error');
    } else {
      mostrarToast('Error al guardar los cambios', 'error');
    }
  } catch (error) {
    console.error('Error guardando cambios:', error);
    mostrarToast('Error de conexión', 'error');
  } finally {
    btn.disabled = false;
    btn.innerHTML = '<i class="fa-solid fa-floppy-disk"></i> Guardar';
  }
}

function actualizarFilaVisual(fila, juego) {
  const select = fila.querySelector('select[data-campo="disponible"]');
  const precioInput = fila.querySelector('input[data-campo="precio"]');

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
