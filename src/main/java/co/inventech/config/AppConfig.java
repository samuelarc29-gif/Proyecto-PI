package co.inventech.config;

import java.io.InputStream;
import java.util.Properties;

/** Carga la configuración desde /config.properties (classpath) o variables de entorno. */
public final class AppConfig {
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = AppConfig.class.getResourceAsStream("/config.properties")) {
            if (in != null) PROPS.load(in);
        } catch (Exception ignored) {}
    }

    public static String get(String key, String def) {
        String v = System.getenv(key);
        if (v != null && !v.isBlank()) return v;
        return PROPS.getProperty(key, def);
    }

    public static String dbUrl() {
    return get("DB_URL",
    "jdbc:mysql://localhost:3306/gestion_activos_tecnologicos?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");}
    public static String dbUser() {
    return get("DB_USER", "root");}
    public static String dbPass() {
    return get("DB_PASS", "Isabel.24");}
    private AppConfig() {}
}
