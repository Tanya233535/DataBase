package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCatsTable {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "postgres";
    private static final String DATABASE_NAME = "My_cats";

    public static void main(String[] args) {
        createCatsTable();
    }

    public static void createCatsTable() {
        String createTableQuery = """
                CREATE TABLE IF NOT EXISTS cats (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    name VARCHAR(20) NOT NULL,
                    type_id INT NOT NULL,
                    age INT NOT NULL,
                    weight DOUBLE NOT NULL,
                    FOREIGN KEY (type_id) REFERENCES types(id)
                )
                """;

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement statement = connection.createStatement()) {

            // Выбор базы данных
            statement.executeUpdate("USE " + DATABASE_NAME);

            // Создание таблицы cats, если она не существует
            statement.executeUpdate(createTableQuery);

            System.out.println("Таблица cats успешно создана!");

        } catch (SQLException e) {
            System.err.println("Ошибка при создании таблицы cats: " + e.getMessage());
        }
    }
}

