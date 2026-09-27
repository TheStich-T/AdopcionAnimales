package org.ap.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

    private static Conexion instancia;
    private static final String CONFIG_FILE = "/db.properties";

    private final String url;
    private final String user;
    private final String password;

    private Conexion() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Properties properties = new Properties();

            try (InputStream input = getClass().getResourceAsStream(CONFIG_FILE)) {
                if (input == null) {
                    throw new RuntimeException("No se encontró el archivo db.properties");
                }
                properties.load(input);
            }

            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.user");
            this.password = properties.getProperty("db.password");

        } catch (ClassNotFoundException | IOException e) {
            throw new RuntimeException("Error al cargar la configuración de la base de datos", e);
        }
    }

    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}