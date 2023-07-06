package Utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbHelper {
    private static Connection conn = null;

    public static Connection getConnection() throws Exception {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(AppConfig.get().getConnectionString());
        }

        return conn;
    }

    public static void closeConnection() throws Exception {
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }
}
