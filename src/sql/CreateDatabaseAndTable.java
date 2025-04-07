package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class CreateDatabaseAndTable {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "postgres";
    private static final String DATABASE_NAME = "My_cats";

    public static void main(String[] args) {
        // SQL-запросы для создания базы данных и таблицы
        String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
        String useDatabaseQuery = "USE " + DATABASE_NAME;
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS types (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    type VARCHAR(100) NOT NULL
                )
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Создание базы данных, если она не существует
            statement.executeUpdate(createDatabaseQuery);

            // Выбор созданной базы данных
            statement.executeUpdate(useDatabaseQuery);

            // Создание таблицы types, если она не существует
            statement.executeUpdate(createTableQuery);

            System.out.println("База данных и таблица успешно созданы!");

            // Добавление пород кошек
            insert_type(connection, "Абиссинская кошка");
            insert_type(connection, "Австралийский мист");
            insert_type(connection, "Американская жесткошерстная");

            System.out.println("Породы успешно добавлены!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция для добавления типа (породы) кошки в таблицу types.
     * @param connection Соединение с базой данных.
     * @param type Название породы кошки.
     */
    public static void insert_type(Connection connection, String type) {
        String insertQuery = "INSERT INTO types (type) VALUES (?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            preparedStatement.setString(1, type);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
