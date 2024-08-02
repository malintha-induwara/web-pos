package lk.ijse.webpos.backend.util;


import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLUtil {
    private static final DataSource dataSource;

    static {
        try {
            dataSource = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/webPos");
        } catch (NamingException e) {
            throw new RuntimeException("Failed to lookup DataSource", e);
        }
    }


    public static <T> T execute(String sql, Object... args) throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                pstm.setObject(i + 1, args[i]);
            }
            if (sql.trim().toLowerCase().startsWith("select")) {
                ResultSet resultSet = pstm.executeQuery();
                List<Map<Integer, Object>> resultCopy = copyResultSet(resultSet);
                return (T) resultCopy;
            } else {
                return (T) (Boolean) (pstm.executeUpdate() > 0);
            }
        }
    }



    private static List<Map<Integer, Object>> copyResultSet(ResultSet resultSet) throws SQLException {
        List<Map<Integer, Object>> rows = new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Map<Integer, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                row.put(i, resultSet.getObject(i));
            }
            rows.add(row);
        }
        return rows;
    }


}

