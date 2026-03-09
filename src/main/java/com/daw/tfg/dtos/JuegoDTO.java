package com.daw.tfg.dtos;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class JuegoDTO {

    private Long idJuego;

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser positivo")
    private Float precio;

    private String descripcion;

    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    private LocalDateTime fechaLanzamiento;

    private String requerimientos;

    private String imagen;

    private Set<GeneroDTO> generos;

    private Long desarrolladorId;

    private Long editorId;

    private String tipo;

    private Long juegoPadreId;

    // Getters y Setters

    public Long getIdJuego() {
        return idJuego;
    }

    public void setIdJuego(Long idJuego) {
        this.idJuego = idJuego;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Float getPrecio() {
        return precio;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public void setFechaLanzamiento(LocalDateTime fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public String getRequerimientos() {
        return requerimientos;
    }

    public void setRequerimientos(String requerimientos) {
        this.requerimientos = requerimientos;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public Set<GeneroDTO> getGeneros() {
        return generos;
    }

    public void setGeneros(Set<GeneroDTO> generos) {
        this.generos = generos;
    }

    public Long getDesarrolladorId() {
        return desarrolladorId;
    }

    public void setDesarrolladorId(Long desarrolladorId) {
        this.desarrolladorId = desarrolladorId;
    }

    public Long getEditorId() {
        return editorId;
    }

    public void setEditorId(Long editorId) {
        this.editorId = editorId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Long getJuegoPadreId() {
        return juegoPadreId;
    }

    public void setJuegoPadreId(Long juegoPadreId) {
        this.juegoPadreId = juegoPadreId;
    }
}

