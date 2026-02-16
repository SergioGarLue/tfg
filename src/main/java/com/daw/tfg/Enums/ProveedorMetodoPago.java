package com.daw.tfg.enums;

public enum ProveedorMetodoPago {
    REDSYS, // para tarjetas -> se puede cambiar por STRIPE que es internacional
    PAYPAL, // para paypal
    BIZUM, // para bizum
    MONEDERO
}
