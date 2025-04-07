package sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

public class CreateCatsTable {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "postgres";
    private static final String DATABASE_NAME = "My_cats";

    static String[] names = {
            "Гарфилд",
            "Том",
            "Гудвин",
            "Рокки",
            "Ленивец",
            "Пушок",
            "Спорти",
            "Бегемот",
            "Пират",
            "Гудини",
            "Зорро",
            "Саймон",
            "Альбус",
            "Базилио",
            "Леопольд",
            "Нарцисс",
            "Атос",
            "Каспер",
            "Валлито",
            "Оксфорд",
            "Бисквит",
            "Соня",
            "Клеопатра",
            "Цунами",
            "Забияка",
            "Матильда",
            "Кнопка",
            "Масяня",
            "Царапка",
            "Серсея",
            "Ворсинка",
            "Амели",
            "Наоми",
            "Маркиза",
            "Изольда",
            "Вальс",
            "Несквик",
            "Златан",
            "Баскет",
            "Изюм",
            "Цукат",
            "Мокко",
            "Месси",
            "Кокос",
            "Адидас",
            "Бейлиз",
            "Тайгер",
            "Зефир",
            "Мохи",
            "Валенсия",
            "Баунти",
            "Свити",
            "Текила",
            "Ириска",
            "Карамель",
            "Виски",
            "Кукуруза",
            "Гренка",
            "Фасолька",
            "Льдинка",
            "Китана",
            "Офелия",
            "Дайкири",
            "Брусника",
            "Аватар",
            "Космос",
            "Призрак",
            "Изумруд",
            "Плинтус",
            "Яндекс",
            "Крисмас",
            "Метеор",
            "Оптимус",
            "Смайлик",
            "Цельсий",
            "Краска",
            "Дейзи",
            "Пенка",
            "Веста",
            "Астра",
            "Эйприл",
            "Среда",
            "Бусинка",
            "Гайка",
            "Елка",
            "Золушка",
            "Мята",
            "Радость",
            "Сиам",
            "Оскар",
            "Феликс",
            "Гарри",
            "Байрон",
            "Чарли",
            "Симба",
            "Тао",
            "Абу",
            "Ватсон",
            "Енисей",
            "Измир",
            "Кайзер",
            "Васаби",
            "Байкал",
            "Багира",
            "Айрис",
            "Диана",
            "Мими",
            "Сакура",
            "Индия",
            "Тиффани",
            "Скарлетт",
            "Пикси",
            "Лиззи",
            "Алиса",
            "Лило",
            "Ямайка",
            "Пэрис",
            "Мальта",
            "Аляска"
    };

    public static void main(String[] args) {
        createCatsTable();

        // Добавление котиков
        insert_cat("Барсик", "Абиссинская кошка", 3, 4.5);
        insert_cat("Муська", "Бенгальская кошка", 5, 3.8);
        insert_cat("Снежок", "Уличный кот", 2, 5.0); // Тип "Уличный кот" отсутствует в таблице types

        add_more_cats(5000);

        // Тестирование функций удаления и обновления котиков
        delete_cat(2); // Удаление котика с id = 2
        delete_cat("age > 4"); // Удаление котиков старше 4 лет
        update_cat(1, "name = 'НовоеИмя', age = 4", "id = 1"); // Обновление имени и возраста котика с id = 1
    }

    public static void createCatsTable() {
        String createTableQuery =
                """
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
     *
     * @param name   Имя кошки.
     * @param type   Тип кошки.
     * @param age    Возраст кошки.
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
     *
     * @param connection Соединение с базой данных.
     * @param type       Название типа кошки.
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
     *
     * @param connection Соединение с базой данных.
     * @param type       Название типа кошки.
     * @return ID добавленного типа кошки.
     */
    private static int insertType(Connection connection, String type) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("USE " + DATABASE_NAME);
        String insertTypeQuery = "INSERT INTO types (type) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertTypeQuery,
                Statement.RETURN_GENERATED_KEYS)) {
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

    /**
     * Функция для добавления в базу данных n случайных кошек.
     *
     * @param n Количество кошек для добавления.
     */
    public static void add_more_cats(int n) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + DATABASE_NAME);

            // Получаем все типы кошек из БД
            String[] catTypes = getAllCatTypes(connection);

            Random random = new Random();
            for (int i = 0; i < n; i++) {
                String name = names[random.nextInt(names.length)];
                String type = catTypes[random.nextInt(catTypes.length)];
                int age = random.nextInt(15) + 1; // Возраст от 1 до 15 лет
                double weight = 2.0 + (8.0 * random.nextDouble()); // Вес от 2 до 10 кг

                insert_cat(name, type, age, weight);
            }
            System.out.println(n + " случайных кошек успешно добавлены!");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении случайных кошек: " + e.getMessage());
        }
    }

    /**
     * Функция для получения всех типов кошек из таблицы types.
     *
     * @param connection Соединение с базой данных.
     * @return Массив типов кошек.
     */
    private static String[] getAllCatTypes(Connection connection) throws SQLException {
        String selectTypesQuery = "SELECT type FROM types";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectTypesQuery)) {
            java.util.List<String> typesList = new java.util.ArrayList<>();
            while (resultSet.next()) {
                typesList.add(resultSet.getString("type"));
            }
            return typesList.toArray(new String[0]);
        }
    }

    /**
     * Функция для удаления котика из таблицы cats по id.
     *
     * @param id ID котика для удаления.
     */
    public static void delete_cat(int id) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + DATABASE_NAME);

            String deleteQuery = "DELETE FROM cats WHERE id = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    System.out.println("Котик с id " + id + " успешно удален!");
                } else {
                    System.out.println("Котик с id " + id + " не найден!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении котика с id " + id + ": " + e.getMessage());
        }
    }

    /**
     * Функция для удаления котиков из таблицы cats по условию WHERE.
     *
     * @param where Условие WHERE для удаления котиков.
     */
    public static void delete_cat(String where) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + DATABASE_NAME);

            String deleteQuery = "DELETE FROM cats WHERE " + where;
            try (Statement preparedStatement = connection.createStatement()) {
                int rowsAffected = preparedStatement.executeUpdate(deleteQuery);
                if (rowsAffected > 0) {
                    System.out.println(rowsAffected + " котиков успешно удалены по условию: " + where);
                } else {
                    System.out.println("Котики не найдены по условию: " + where);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении котиков по условию " + where + ": " + e.getMessage());
        }
    }

    /**
     * Функция для обновления данных котика в таблице cats.
     *
     * @param id    ID котика для обновления.
     * @param set   Строка со значениями, которые нужно обновить (например, "name = 'Мурзик', age = 3").
     * @param where Условие WHERE для обновления котика.
     */
    public static void update_cat(int id, String set, String where) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            statement.executeUpdate("USE " + DATABASE_NAME);

            String updateQuery = "UPDATE cats SET " + set + " WHERE " + where;
            try (Statement preparedStatement = connection.createStatement()) {
                int rowsAffected = preparedStatement.executeUpdate(updateQuery);
                if (rowsAffected > 0) {
                    System.out.println("Данные котика с id " + id + " успешно обновлены!");
                } else {
                    System.out.println("Котик с id " + id + " не найден!");
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении данных котика с id " + id + ": " + e.getMessage());
        }
    }
}
