package com.inventech.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase centralizada para la conexión a MySQL.
 * Lee credenciales desde src/main/resources/db.properties.
 * Patrón Singleton ligero: cada llamada abre/devuelve una conexión válida.
 */
public class ConexionBD {

    private static String url;
    private static String user;
    private static String password;
    private static boolean cargado = false;

    private ConexionBD() {}

    private static synchronized void cargarConfig() {
        if (cargado) return;
        Properties props = new Properties();
        try (InputStream in = ConexionBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("No se encontró db.properties en resources");
            }
            props.load(in);
            Class.forName(props.getProperty("db.driver"));
            url = props.getProperty("db.url");
            user = props.getProperty("db.user");
            password = props.getProperty("db.password");
            cargado = true;
        } catch (Exception e) {
            throw new RuntimeException("Error cargando configuración BD: " + e.getMessage(), e);
        }
    }

    public static Connection getConnection() throws SQLException {
        cargarConfig();
        return DriverManager.getConnection(url, user, password);
    }

    public static boolean probarConexion() {
        try (Connection c = getConnection()) {
            return c != null && !c.isClosed();
        } catch (SQLException e) {
            System.err.println("Conexión falló: " + e.getMessage());
            return false;
        }
    }
}
