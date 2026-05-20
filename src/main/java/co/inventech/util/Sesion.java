package co.inventech.util;

import co.inventech.model.Usuario;
import java.util.HashSet;
import java.util.Set;

/** Sesión global del usuario autenticado. */
public final class Sesion {
    private static Usuario usuario;
    private static final Set<String> permisos = new HashSet<>();

    public static void iniciar(Usuario u, Set<String> perms) {
        usuario = u;
        permisos.clear();
        permisos.addAll(perms);
    }
    public static void cerrar() { usuario = null; permisos.clear(); }
    public static Usuario usuario() { return usuario; }
    public static boolean activa() { return usuario != null; }
    public static boolean tienePermiso(String p) { return permisos.contains(p); }
    public static Set<String> permisos() { return permisos; }

    private Sesion() {}
}
