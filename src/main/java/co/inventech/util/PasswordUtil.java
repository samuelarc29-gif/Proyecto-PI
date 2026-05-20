package co.inventech.util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {
    public static String hash(String plain) { return BCrypt.hashpw(plain, BCrypt.gensalt(11)); }
    public static boolean verify(String plain, String hash) {
        if (hash == null || hash.isEmpty()) return false;
        // Soporte para contraseñas en texto plano (datos de demostración)
        if (!hash.startsWith("$2")) return plain.equals(hash);
        try { return BCrypt.checkpw(plain, hash); } catch (Exception e) { return false; }
    }
    private PasswordUtil() {}
}
