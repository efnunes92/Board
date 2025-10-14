package br.com.erikferreira.persistence.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.h2.tools.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ConnectionConfig {

    private static final String JDBC_URL = "jdbc:h2:mem:board;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    private static Server webServer;

    public static void startWebConsole() throws SQLException {
        if (webServer == null || !webServer.isRunning(false)) {
            webServer = Server.createWebServer(
                    "-web",
                    "-webAllowOthers",
                    "-webPort", "8082"
            ).start();
            System.out.println("H2 Web Console iniciado: http://localhost:8082");
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void stopWebConsole() {
        if (webServer != null) {
            webServer.stop();
            System.out.println("H2 Web Console parado");
        }
    }
}
