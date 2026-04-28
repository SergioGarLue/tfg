package com.daw.tfg.service;

import com.daw.tfg.dtos.CompraDTO;
import com.daw.tfg.enums.EstadoCompra;
import com.daw.tfg.models.Compra;
import java.util.List;

public interface ICompraService {
    CompraDTO procesarCompra(Long usuarioId);
    List<CompraDTO> getHistorialPorUsuario(Long usuarioId);
    CompraDTO getDetalleCompra(Long compraId);
    Compra actualizarEstadoCompraPorPaymentIntent(String paymentIntentId, EstadoCompra estado);
}

