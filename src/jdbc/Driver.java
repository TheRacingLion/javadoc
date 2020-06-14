package jdbc;

import java.sql.DriverManager;
import java.sql.Connection;

/**
 * Handles connection with database by creating a {@code Connection} with the SQL server info given.
 *
 * @see java.sql.Connection
 */
class Driver {
    /** SQL server URL */
    private static final String URL = "jdbc:sqlserver://";
    /** SQL server IP */
    private static final String SERVER_IP = "*";
    /** SQL server port */
    private static final String SERVER_PORT = "*";
    /** SQL server database name */
    private static final String DB_NAME = "*";
    /** SQL server username */
    private static final String USER_NAME = "*";
    /** SQL server password */
    private static final String PASSWORD = "*";

    /**
     * Get the full connection URL for the SQL server
     *
     * @return The {@code String} URL
     */
    private static String getConnectionUrl() {
        return URL + SERVER_IP + ":" + SERVER_PORT + ";databaseName=" + DB_NAME;
    }

    /**
     * Create a {@code Connection} for the current SQL server
     *
     * @return The {@code Connection} created. Null if not a valid {@code Connection}.
     */
    static Connection getConnection() {
        try {
            Connection con = DriverManager.getConnection(getConnectionUrl(), USER_NAME, PASSWORD);
            if (con != null) return con;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error Trace in getConnection() : " + e.getMessage());
        }
        return null;
    }
}
