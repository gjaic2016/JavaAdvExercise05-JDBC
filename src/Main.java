import java.sql.*;
import java.util.Scanner;

public class Main {
    final static String URLLINK = "jdbc:mysql://localhost:3306/javatest";
    final static String USER = "admin";
    final static String PASSWORD = "admin";

    public static void main(String[] args) throws SQLException {

        int selection;
        Scanner scanner = new Scanner(System.in);
        do {
            printMenu();
            selection = scanner.nextInt();
            scanner.nextLine();
            switch (selection) {
                case 1 -> getAllusers();
                case 2 -> insertUser(scanner);
                case 3 -> deleteUser(scanner);
                case 4 -> updateUser(scanner);
            }


        } while (selection != 5);

    }

    private static void printMenu() {
        System.out.println("1. Print all users");
        System.out.println("2. Insert user");
        System.out.println("3. Remove user by id");
        System.out.println("4. Update user by id");
        System.out.println("5. Exit");
        System.out.println("_______________________");
    }

    private static void insertUser(Scanner scanner) throws SQLException {

        System.out.println("insert user function....");

        System.out.println("Enter user: ");
        String user = scanner.next();

        System.out.println("Enter user age: ");
        int age = scanner.nextInt();

        try (Connection conn = DriverManager.getConnection(URLLINK, USER, PASSWORD)) {

            PreparedStatement ps = conn.prepareStatement("INSERT INTO users (name, age) VALUES (?, ?)");

            ps.setString(1, user);
            ps.setInt(2, age);

            System.out.println("SQL statement successful." + ps.executeUpdate());
        }

        System.out.println("Finished inserting user.");

    }

    private static void getAllusers() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URLLINK, USER, PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement("SELECT * FROM users");
            ps.getResultSet();

            if (ps.execute()) {
                ResultSet rs = ps.getResultSet();
                while (rs.next()) {
                    System.out.print("Id: " + rs.getInt(1)); //gets the first column's rows.
                    System.out.print(", user: " + rs.getString(2)); //gets the second column's rows.
                    System.out.println(", age: " + rs.getInt(3)); //gets the third column's rows.

                }
            } else {
                System.out.println("No results.");
            }
        }

    }

    private static void deleteUser(Scanner scanner) throws SQLException {

        System.out.println("Enter id of user to delete: ");
        int idToDelete = scanner.nextInt();
        System.out.println("---------------------->idToDelete: " + idToDelete);

        try (Connection conn = DriverManager.getConnection(URLLINK, USER, PASSWORD)) {

            PreparedStatement ps = conn.prepareStatement("DELETE FROM users WHERE id =?");
            ps.setInt(1, idToDelete);
            ps.executeUpdate();

        }
        System.out.println("User with id: " + idToDelete + " has been deleted.");
    }

    private static void updateUser(Scanner scanner) {

        System.out.println("insert user function....");

        System.out.println("Enter new user name: ");
        String username = scanner.next();

        System.out.println("Enter new user age: ");
        int age = scanner.nextInt();

        System.out.println("Enter id of user to change name: ");
        int idIndex = scanner.nextInt();

        try (Connection conn = DriverManager.getConnection(URLLINK, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try {

                PreparedStatement ps = conn.prepareStatement("UPDATE users SET name = ?, age = ? WHERE id = ?");

                ps.setString(1, username);
                ps.setInt(2, age);
                ps.setInt(3, idIndex);

                ps.executeUpdate();

                System.out.println("Committing transaction...");
                conn.commit();
            } catch (SQLException se) {

                System.out.println("SQLException caught. Rolling back transaction...");
                se.printStackTrace();
            }

            System.out.println("Transaction completed.");
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
}