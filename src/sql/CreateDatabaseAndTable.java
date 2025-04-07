package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateDatabaseAndTable {
    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "postgres";
    private static final String DATABASE_NAME = "My_cats";

    static String[] types = new String[]{"Абиссинская кошка",
            "Австралийский мист",
            "Американская жесткошерстная",
            "Американская короткошерстная",
            "Американский бобтейл",
            "Американский кёрл",
            "Балинезийская кошка",
            "Бенгальская кошка",
            "Бирманская кошка",
            "Бомбейская кошка",
            "Бразильская короткошёрстная",
            "Британская длинношерстная",
            "Британская короткошерстная",
            "Бурманская кошка",
            "Бурмилла кошка",
            "Гавана",
            "Гималайская кошка",
            "Девон-рекс",
            "Донской сфинкс",
            "Европейская короткошерстная",
            "Египетская мау",
            "Канадский сфинкс",
            "Кимрик",
            "Корат",
            "Корниш-рекс",
            "Курильский бобтейл",
            "Лаперм",
            "Манчкин",
            "Мейн-ку́н",
            "Меконгский бобтейл",
            "Мэнкс кошка",
            "Наполеон",
            "Немецкий рекс",
            "Нибелунг",
            "Норвежская лесная кошка",
            "Ориентальная кошка",
            "Оцикет",
            "Персидская кошка",
            "Петерболд",
            "Пиксибоб",
            "Рагамаффин",
            "Русская голубая кошка",
            "Рэгдолл",
            "Саванна",
            "Селкирк-рекс",
            "Сиамская кошка",
            "Сибирская кошка",
            "Сингапурская кошка",
            "Скоттиш-фолд",
            "Сноу-шу",
            "Сомалийская кошка",
            "Тайская кошка",
            "Тойгер",
            "Тонкинская кошка",
            "Турецкая ангорская кошка",
            "Турецкий ван",
            "Украинский левкой",
            "Чаузи",
            "Шартрез",
            "Экзотическая короткошерстная",
            "Японский бобтейл"
    };

    public static void main(String[] args) {

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

            statement.executeUpdate(createDatabaseQuery);
            statement.executeUpdate(useDatabaseQuery);
            statement.executeUpdate(createTableQuery);

            System.out.println("База данных и таблица успешно созданы!");

            add_all_types(connection);

            System.out.println("Все породы успешно добавлены!");

            delete_type(connection, 1);
            update_type(connection, 2, "Новая порода");

            System.out.println("Тестирование удаления и обновления завершено!");

            // Тестирование функций получения данных
            System.out.println("Порода с id 3: " + get_type(connection, 3));
            System.out.println("Породы, где id < 15:");
            get_type_where(connection, "id < 15");
            System.out.println("Все породы:");
            get_all_types(connection);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Функция для добавления типа (породы) кошки в таблицу types.
     *
     * @param connection Соединение с базой данных.
     * @param type       Название породы кошки.
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

    /**
     * Функция для добавления всех пород кошек из списка в таблицу types.
     * @param connection Соединение с базой данных.
     */
    public static void add_all_types(Connection connection) {
        for (String type : types) {
            insert_type(connection, type);
        }
    }

    /**
     * Функция для удаления типа (породы) кошки из таблицы types.
     * @param connection Соединение с базой данных.
     * @param id ID породы кошки для удаления.
     */
    public static void delete_type(Connection connection, int id) {
        String deleteQuery = "DELETE FROM types WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
            System.out.println("Порода с id " + id + " успешно удалена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении породы с id " + id + ": " + e.getMessage());
        }
    }

    /**
     * Функция для обновления типа (породы) кошки в таблице types.
     * @param connection Соединение с базой данных.
     * @param id ID породы кошки для обновления.
     * @param new_type Новое название породы кошки.
     */
    public static void update_type(Connection connection, int id, String new_type) {
        String updateQuery = "UPDATE types SET type = ? WHERE id = ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, new_type);
            preparedStatement.setInt(2, id);
            preparedStatement.executeUpdate();
            System.out.println("Порода с id " + id + " успешно обновлена на " + new_type + ".");
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении породы с id " + id + ": " + e.getMessage());
        }
    }

    /**
     * Функция для получения типа (породы) кошки по ID.
     * @param connection Соединение с базой данных.
     * @param id ID породы кошки.
     * @return Название породы кошки или null, если порода не найдена.
     */
    public static String get_type(Connection connection, int id) {
        String selectQuery = "SELECT type FROM types WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("type");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении породы с id " + id + ": " + e.getMessage());
        }
        return null;
    }

    /**
     * Функция для вывода на экран всех пород кошек, соответствующих условию WHERE.
     * @param connection Соединение с базой данных.
     * @param where Условие WHERE для запроса.
     */
    public static void get_type_where(Connection connection, String where) {
        String selectQuery = "SELECT * FROM types WHERE " + where;
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Type: " + resultSet.getString("type"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении пород по условию " + where + ": " + e.getMessage());
        }
    }

    /**
     * Функция для вывода на экран всех пород кошек.
     * @param connection Соединение с базой данных.
     */
    public static void get_all_types(Connection connection) {
        String selectQuery = "SELECT * FROM types";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectQuery)) {
            while (resultSet.next()) {
                System.out.println("ID: " + resultSet.getInt("id") + ", Type: " + resultSet.getString("type"));
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении всех пород: " + e.getMessage());
        }
    }
}
