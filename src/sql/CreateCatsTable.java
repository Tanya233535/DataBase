package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateCatsTable {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "postgres";
    private static final String DATABASE_NAME = "My_cats";

    public static void main(String[] args) {
        createCatsTable();

        // Добавление котиков
        insert_cat("Барсик", "Абиссинская кошка", 3, 4.5);
        insert_cat("Муська", "Бенгальская кошка", 5, 3.8);
        insert_cat("Снежок", "Уличный кот", 2, 5.0); // Тип "Уличный кот" отсутствует в таблице types
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

    /**
     * Функция для добавления кошки в таблицу cats.
     * @param name Имя кошки.
     * @param type Тип кошки.
     * @param age Возраст кошки.
     * @param weight Вес кошки.
     */
    public static void insert_cat(String name, String type, int age, Double weight) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + DATABASE_NAME);

            // Получаем id типа кошки
            int typeId = getTypeID(connection, type);

            // Если такого типа нет, добавляем его в таблицу types
            if (typeId == -1) {
                typeId = insertType(connection, type);
            }

            // Подготавливаем запрос для добавления кошки
            String insertCatQuery = "INSERT INTO cats (name, type_id, age, weight) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertCatQuery)) {
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, typeId);
                preparedStatement.setInt(3, age);
                preparedStatement.setDouble(4, weight);

                // Выполняем запрос
                preparedStatement.executeUpdate();
                System.out.println("Кошка " + name + " успешно добавлена!");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении кошки: " + e.getMessage());
        }
    }

    /**
     * Функция для получения id типа кошки по названию.
     * @param connection Соединение с базой данных.
     * @param type Название типа кошки.
     * @return ID типа кошки или -1, если тип не найден.
     */
    private static int getTypeID(Connection connection, String type) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("USE " + DATABASE_NAME);
        String selectTypeQuery = "SELECT id FROM types WHERE type = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectTypeQuery)) {
            preparedStatement.setString(1, type);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        }
        return -1;
    }

    /**
     * Функция для добавления нового типа кошки в таблицу types.
     * @param connection Соединение с базой данных.
     * @param type Название типа кошки.
     * @return ID добавленного типа кошки.
     */
    private static int insertType(Connection connection, String type) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("USE " + DATABASE_NAME);
        String insertTypeQuery = "INSERT INTO types (type) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTypeQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, type);
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Не удалось получить ID добавленного типа.");
                }
            }
        }
    }
}
