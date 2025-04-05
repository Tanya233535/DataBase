import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class SimpleBookDatabase {
    public static void main(String[] args) {
        // Параметры подключения к MySQL
        String url = "jdbc:mysql://localhost:3306/books";
        String user = "root";
        String password = "postgres";


        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS books (
                id INT AUTO_INCREMENT PRIMARY KEY,
                title VARCHAR(255) NOT NULL,
                year INT,
                author VARCHAR(255) NOT NULL
            );
            """;


        String insertDataSQL1 = "INSERT INTO books (title, year, author) VALUES ('1984', 1949, 'George Orwell');";
        String insertDataSQL2 = "INSERT INTO books (title, year, author) VALUES ('To Kill a Mockingbird', 1960, 'Harper Lee');";
        String insertDataSQL3 = "INSERT INTO books (title, year, author) VALUES ('The Great Gatsby', 1925, 'F. Scott Fitzgerald');";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createTableSQL);
            System.out.println("Таблица 'books' успешно создана!");

            stmt.execute(insertDataSQL1);
            stmt.execute(insertDataSQL2);
            stmt.execute(insertDataSQL3);
            System.out.println("Данные успешно добавлены!");


            String selectSQL = "SELECT * FROM books;";
            ResultSet rs = stmt.executeQuery(selectSQL);

            System.out.println("\nСписок книг:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String author = rs.getString("author");

                System.out.printf("ID: %d | Название: %s | Год: %d | Автор: %s%n", id, title, year, author);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }
}
