package com.daw.tfg.dtos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JuegoDTO {
    @JsonProperty("appid")
    private Long idJuego;

    @JsonProperty("name")
    private String titulo;

    private String type;

    private String developer;

    private String publisher;

    @JsonProperty("release_date")
    private String fechaLanzamiento;

    private List<String> genres;

    private String description;

    @JsonProperty("header_image")
    private String imagen;

    @JsonProperty("storage_gb")
    private Float pesoJuego;

    private PriceDTO price;

    private PlatformsDTO platforms;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public static class PriceDTO {
        @JsonProperty("final")
        private Double finalPrice;

        @JsonProperty("discount_percent")
        private Integer porcentaje;
    }

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public static class PlatformsDTO {
        private Boolean windows;
        private Boolean mac;
        private Boolean linux;
    }
}
