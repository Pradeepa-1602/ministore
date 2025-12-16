import java.sql.*;
import java.util.Scanner;

public class onestore {

    static final String URL =
        "jdbc:mysql://localhost:3306/onestore?useSSL=false&serverTimezone=UTC";
    static final String USER = "root";
    static final String PASS = "Pradeepa@2006";

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=== ONE STORE ===");
            System.out.println("1. View Available Products");
            System.out.println("2. Place your Order");
            System.out.println("3. Exit");
            System.out.print("Choice: ");

            int ch = sc.nextInt();

            if (ch == 1) viewProducts();
            else if (ch == 2) placeOrder();
            else if (ch == 3) {
                System.out.println("Thank you for purchasing!");
                System.exit(0);
            } else System.out.println("Invalid choice");
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    static void viewProducts() {
        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM products")) {

            System.out.println("\nID  NAME     PRICE");
            while (rs.next()) {
                System.out.println(
                        rs.getInt("id") + "   " +
                        rs.getString("name") + "   " +
                        rs.getDouble("price"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void placeOrder() {
        try (Connection con = getConnection()) {
            System.out.print("Place  order: ");
            System.out.print("Enter Product ID: ");
            int id = sc.nextInt();

            System.out.print("Enter Quantity: ");
            int qty = sc.nextInt();

            PreparedStatement ps =
                con.prepareStatement(
                    "SELECT name, price FROM products WHERE id=?");
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                double total = price * qty;

                PreparedStatement ins =
                    con.prepareStatement(
                        "INSERT INTO orders(product_name, qty, total) VALUES(?,?,?)");
                ins.setString(1, name);
                ins.setInt(2, qty);
                ins.setDouble(3, total);
                ins.executeUpdate();

                System.out.println("Order Successful!");
                System.out.println("Total Amount: " + total);
            } else {
                System.out.println("Product not found!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
//javac -cp ".;mysql-connector-j-9.5.0.jar" onestore.java
//java -cp ".;mysql-connector-j-9.5.0.jar" onestore


