package br.com.erikferreira;

import br.com.erikferreira.persistence.config.ConnectionConfig;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            ConnectionConfig.startWebConsole();
            try (Connection connection = ConnectionConfig.getConnection()) {
                Liquibase liquibase = new Liquibase(
                        "db.changelog/migrations/db.changelog-202510131450.sql",
                        new ClassLoaderResourceAccessor(),
                        new JdbcConnection(connection)
                );
                liquibase.update(new Contexts(), new LabelExpression());

                System.out.println("Migrations executadas com sucesso!");
            }
            System.out.println("Aplicação rodando. Pressione Ctrl+C para sair.");
            Thread.currentThread().join();

        } catch (SQLException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro ao executar Liquibase:");
            e.printStackTrace();
        } finally {
            ConnectionConfig.stopWebConsole();
        }
    }
}
