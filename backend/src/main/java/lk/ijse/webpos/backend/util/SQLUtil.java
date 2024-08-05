package lk.ijse.webpos.backend.util;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLUtil {
    private static final DataSource dataSource;

    static {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/webPos");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup DataSource", e);
        }
    }


    public static <T> T execute(Connection connection, String sql, Object... args) throws SQLException {
        PreparedStatement pstm = connection.prepareStatement(sql);
        for (int i = 0; i < args.length; i++) {
            pstm.setObject(i + 1, args[i]);
        }
        if (sql.trim().toLowerCase().startsWith("select")) {
            return (T) pstm.executeQuery();
        } else {
            return (T) (Boolean) (pstm.executeUpdate() > 0);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

