package com.inventech.util;

import com.inventech.model.Usuario;


public class Sesion {
    private static Usuario usuarioActual;

    public static void setUsuario(Usuario u) { usuarioActual = u; }
    public static Usuario getUsuario() { return usuarioActual; }
    public static String getRol() { return usuarioActual == null ? null : usuarioActual.getNombreRol(); }
    public static void cerrar() { usuarioActual = null; }
}
