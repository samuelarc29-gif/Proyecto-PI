package co.inventech.db;

import co.inventech.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Fábrica de conexiones JDBC a MySQL. */
public final class ConexionDB {
    static {
        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) { throw new RuntimeException("Driver MySQL no encontrado", e); }
    }

    public static Connection get() throws SQLException {
        return DriverManager.getConnection(AppConfig.dbUrl(), AppConfig.dbUser(), AppConfig.dbPass());
    }

    private ConexionDB() {}
}
