import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class SimpleBookDatabase2 {
    public static void main(String[] args) {
        // Параметры подключения к MySQL
        String url = "jdbc:mysql://localhost:3306/books";
        String user = "root";
        String password = "postgres";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement()) {

            String createGenresTableSQL = """
                CREATE TABLE IF NOT EXISTS genres (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    genre VARCHAR(255) NOT NULL
                );
                """;
            stmt.execute(createGenresTableSQL);
            System.out.println("Таблица 'genres' успешно создана!");


            String insertGenresSQL1 = "INSERT INTO genres (genre) VALUES ('Science Fiction');";
            String insertGenresSQL2 = "INSERT INTO genres (genre) VALUES ('Classic');";
            String insertGenresSQL3 = "INSERT INTO genres (genre) VALUES ('Drama');";
            stmt.execute(insertGenresSQL1);
            stmt.execute(insertGenresSQL2);
            stmt.execute(insertGenresSQL3);
            System.out.println("Данные в таблицу 'genres' успешно добавлены!");

            String addGenreIdColumnSQL = """
                ALTER TABLE books
                ADD COLUMN genre_id INT;
                """;
            stmt.execute(addGenreIdColumnSQL);
            System.out.println("Столбец 'genre_id' успешно добавлен в таблицу 'books'!");

            // Установка внешнего ключа между books.genre_id и genres.id
            String addForeignKeySQL = """
                ALTER TABLE books
                ADD CONSTRAINT fk_genre
                FOREIGN KEY (genre_id)
                REFERENCES genres(id);
                """;
            stmt.execute(addForeignKeySQL);
            System.out.println("Связь между таблицами 'books' и 'genres' успешно установлена!");

            // Обновление данных в таблице books для связывания с жанрами
            String updateBooksSQL1 = "UPDATE books SET genre_id = 1 WHERE title = '1984';";
            String updateBooksSQL2 = "UPDATE books SET genre_id = 2 WHERE title = 'To Kill a Mockingbird';";
            String updateBooksSQL3 = "UPDATE books SET genre_id = 3 WHERE title = 'The Great Gatsby';";
            stmt.execute(updateBooksSQL1);
            stmt.execute(updateBooksSQL2);
            stmt.execute(updateBooksSQL3);
            System.out.println("Данные в таблице 'books' успешно обновлены!");

            // Извлечение данных из обеих таблиц с использованием JOIN
            String selectSQL = """
                SELECT books.id, books.title, books.year, books.author, genres.genre
                FROM books
                JOIN genres ON books.genre_id = genres.id;
                """;
            ResultSet rs = stmt.executeQuery(selectSQL);

            System.out.println("\nСписок книг с жанрами:");
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                int year = rs.getInt("year");
                String author = rs.getString("author");
                String genre = rs.getString("genre");

                System.out.printf("ID: %d | Название: %s | Год: %d | Автор: %s | Жанр: %s%n", id, title, year, author, genre);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при работе с базой данных: " + e.getMessage());
        }
    }
}
