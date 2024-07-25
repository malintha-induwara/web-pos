package lk.ijse.webpos.backend.util;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {
//    private static DataSource dataSource;
//
//    static {
//        try {
//            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/webPos");
//        } catch (NamingException e) {
//            throw new RuntimeException("Failed to lookup DataSource", e);
//        }
//    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load MySQL JDBC driver", e);
        }
    }

    public static <T> T execute(String sql, Object... args) throws SQLException {
        try  {
            Connection connection = getConnection();
            PreparedStatement pstm = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstm.setObject(i + 1, args[i]);
            }

            if (sql.trim().toLowerCase().startsWith("select")) {
                return (T) pstm.executeQuery();
            } else {
                return (T) (Boolean) (pstm.executeUpdate() > 0);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/webPos";
        String user = "root";
        String password = "Syrex@1234";
        return DriverManager.getConnection(url, user, password);
    }



}

