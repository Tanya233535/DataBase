package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class CreateDatabaseAndTable {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "postgres";

        String databaseName = "My_cats";

        // SQL-запросы для создания базы данных и таблицы
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + databaseName;
        String useDatabaseQuery = "USE " + databaseName;
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS types (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    type VARCHAR(100) NOT NULL
                )
                """;

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement()) {

            // Создание базы данных, если она не существует
            statement.executeUpdate(createDatabaseQuery);

            // Выбор созданной базы данных
            statement.executeUpdate(useDatabaseQuery);

            // Создание таблицы types, если она не существует
            statement.executeUpdate(createTableQuery);

            System.out.println("База данных и таблица успешно созданы!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
