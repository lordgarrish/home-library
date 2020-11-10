import java.sql.*;
import java.util.*;


public class Main {
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String selectAllQuery = "SELECT * FROM authors JOIN books USING(author_id) ORDER BY " +
            "author_id";
    private static final String findAuthorQuery = "SELECT * FROM authors JOIN books USING(author_id) WHERE " +
            "first_name = ? AND last_name = ?";
    private static final String addAuthorQuery = "INSERT INTO authors (first_name, last_name) VALUES (?, ?) " +
            "ON CONFLICT(first_name, last_name) DO NOTHING";
    private static final String addBookQuery = "INSERT INTO books (title, genre, author_id) VALUES (?, ?, " +
            "(SELECT author_id FROM authors WHERE first_name = ? AND last_name = ?))";
    private static final String updateBookQuery = "UPDATE books SET title = ? WHERE book_id = ?";
    private static final String deleteBookQuery = "DELETE FROM books WHERE book_id = ?";


    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            Scanner in = new Scanner(System.in);
            boolean done = false;
            while (!done) {
                System.out.println("\nSelect operation:");
                System.out.println("Press 1 to list all books and their authors.");
                System.out.println("Press 2 to find specific author and all of his books.");
                System.out.println("Press 3 to add book to the library.");
                System.out.println("Press 4 to update book's title.");
                System.out.println("Press 5 to delete book from library.");
                System.out.println("Press X to exit\n");
                String input = in.next().toUpperCase();
                switch (input) {
                    case ("1"):
                        selectAll(connection);
                        break;
                    case ("2"):
                        findAuthor(connection);
                        break;
                    case ("3"):
                        insertBook(connection);
                        break;
                    case ("4"):
                        updateBook(connection);
                        break;
                    case ("5"):
                        deleteBook(connection);
                        break;
                    default:
                        done = true;
                        break;
                }
            }
        } catch (SQLException e) {
            for (Throwable t : e)
                System.out.println(t.getMessage());
        }
    }

    private static void selectAll(Connection connection) throws SQLException{
        try (Statement stat = connection.createStatement()) {
            ResultSet resultSet = stat.executeQuery(selectAllQuery);
            System.out.println("ID | Name          | Title       | Genre       | Book ID");
            System.out.println("---------------------------------------------------------------------");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("author_id") + " | " +
                        resultSet.getString("first_name") + " " + resultSet.getString("last_name") +
                        " | " + resultSet.getString("title") + " | " + resultSet.getString("genre")
                        + " | " + resultSet.getString("book_id"));
            }
        }
    }

    private static void findAuthor(Connection connection) throws SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Enter author's first name (e.g. George): ");
        String first = key.nextLine();
        System.out.println("Enter author's last name (e.g. Orwell): ");
        String last = key.nextLine();
        try (PreparedStatement stat = connection.prepareStatement(findAuthorQuery)) {
            stat.setString(1, first);
            stat.setString(2, last);
            ResultSet resultSet = stat.executeQuery();
            System.out.println("ID | Name          | Title       | Genre       | Book ID");
            System.out.println("---------------------------------------------------------------------");
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("author_id") + " | " +
                        resultSet.getString("first_name") + " " + resultSet.getString("last_name") +
                        " | " + resultSet.getString("title") + " | " + resultSet.getString("genre")
                        + " | " + resultSet.getString("book_id"));
            }
        }
    }

    private static void insertBook(Connection connection) throws SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Enter author's first name (e.g. George): ");
        String first = key.nextLine();
        System.out.println("Enter author's last name (e.g. Orwell): ");
        String last = key.nextLine();
        System.out.println("Enter book's title (e.g. 1984): ");
        String title = key.nextLine();
        System.out.println("Enter book's genre (e.g. Novel): ");
        String genre = key.nextLine();
        try (PreparedStatement stat = connection.prepareStatement(addAuthorQuery)) {
            stat.setString(1, first);
            stat.setString(2, last);
        }
        try (PreparedStatement stat = connection.prepareStatement(addBookQuery)) {
            stat.setString(1, title);
            stat.setString(2, genre);
            stat.setString(3, first);
            stat.setString(4, last);
            int r = stat.executeUpdate();
            System.out.println(r + " row(s) inserted");
        }
    }

    private static void updateBook(Connection connection) throws SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Enter book's id: ");
        int id = key.nextInt();
        System.out.println("Enter new book's title (e.g. 1984): ");
        String title = key.next();
        try (PreparedStatement stat = connection.prepareStatement(updateBookQuery)) {
            stat.setString(1, title);
            stat.setInt(2, id);
            int r = stat.executeUpdate();
            System.out.println(r + " row(s) updated");
        }
    }

    private static void deleteBook(Connection connection) throws SQLException {
        Scanner key = new Scanner(System.in);
        System.out.println("Enter book's id: ");
        int id = key.nextInt();
        try (PreparedStatement stat = connection.prepareStatement(deleteBookQuery)) {
            stat.setInt(1, id);
            int r = stat.executeUpdate();
            System.out.println(r + " row(s) deleted");
        }
    }
}
