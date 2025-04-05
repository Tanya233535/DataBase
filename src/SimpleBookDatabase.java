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

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            // Удаление дубликатов из таблицы books
            String deleteDuplicatesSQL = """
                DELETE b1
                FROM books b1
                INNER JOIN (
                    SELECT MIN(id) as min_id, title, year, author
                    FROM books
                    GROUP BY title, year, author
                ) b2
                ON b1.title = b2.title AND b1.year = b2.year AND b1.author = b2.author
                WHERE b1.id > b2.min_id;
                """;
            stmt.execute(deleteDuplicatesSQL);
            System.out.println("Дубликаты успешно удалены!");

            // Удаление новой книги, если она добавлена с genre_id
            String deleteNewBookSQL = "DELETE FROM books WHERE title = 'Pride and Prejudice';";
            stmt.execute(deleteNewBookSQL);
            System.out.println("Новая книга успешно удалена!");

            // Добавление новой книги без дубликатов
            String insertNewBookSQL = "INSERT INTO books (title, year, author) VALUES ('Pride and Prejudice', 1813, 'Jane Austen');";
            stmt.execute(insertNewBookSQL);
            System.out.println("Новая книга успешно добавлена!");

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
