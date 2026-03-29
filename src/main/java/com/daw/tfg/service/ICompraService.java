package com.daw.tfg.service;

import com.daw.tfg.dtos.CompraDTO;
import java.util.List;

public interface ICompraService {
    CompraDTO procesarCompra(Long usuarioId);
    List<CompraDTO> getHistorialPorUsuario(Long usuarioId);
    CompraDTO getDetalleCompra(Long compraId);
}

