import java.sql.*;
public class db {
    private static Connection connection;
    public static Connection configDB() {
        try {
            String url="jdbc:mysql://localhost:3306/db_peternakan"; //url database
            String user="root"; //user database
            String pass=""; //password database
            connection=DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.err.println("koneksi gagal "+e.getMessage()); //perintah menampilkan error pada config
        }
        return connection;
    }
}
