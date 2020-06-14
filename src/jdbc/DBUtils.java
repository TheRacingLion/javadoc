package jdbc;

import java.sql.*;

/**
 * A collection of JDBC helper methods.
 * @author Apache
 */
final class DBUtils {
    /**
     * Close a {@code Connection}, avoid closing if null.
     *
     * @param conn Connection to close.
     * @throws SQLException if a database access error occurs
     */
    static void close(final Connection conn) throws SQLException {
        if (conn != null) conn.close();
    }

    /**
     * Close a {@code ResultSet}, avoid closing if null.
     *
     * @param rs ResultSet to close.
     * @throws SQLException if a database access error occurs
     */
    static void close(final ResultSet rs) throws SQLException {
        if (rs != null) rs.close();
    }

    /**
     * Close a {@code Statement}, avoid closing if null.
     *
     * @param stmt Statement to close.
     * @throws SQLException if a database access error occurs
     */
    static void close(final Statement stmt) throws SQLException {
        if (stmt != null) stmt.close();
    }

    /**
     * Close a {@code Connection}, {@code Statement} and
     * {@code ResultSet}.  Avoid closing if null and hide any
     * SQLExceptions that occur.
     *
     * @param conn Connection to close.
     * @param stmt Statement to close.
     * @param rs ResultSet to close.
     */
    static void closeQuietly(final Connection conn, final Statement stmt, final ResultSet rs) {
        try {
            closeQuietly(rs);
        } finally {
            try {
                closeQuietly(stmt);
            } finally {
                closeQuietly(conn);
            }
        }
    }

    /**
     * Close a {@code Connection}, avoid closing if null and hide
     * any SQLExceptions that occur.
     *
     * @param conn Connection to close.
     */
    static void closeQuietly(final Connection conn) {
        try {
            close(conn);
        } catch (final SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * Close a {@code ResultSet}, avoid closing if null and hide any
     * SQLExceptions that occur.
     *
     * @param rs ResultSet to close.
     */
    static void closeQuietly(final ResultSet rs) {
        try {
            close(rs);
        } catch (final SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * Close a {@code Statement}, avoid closing if null and hide
     * any SQLExceptions that occur.
     *
     * @param stmt Statement to close.
     */
    static void closeQuietly(final Statement stmt) {
        try {
            close(stmt);
        } catch (final SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * Commits a {@code Connection} then closes it, avoid closing if null.
     *
     * @param conn Connection to close.
     * @throws SQLException if a database access error occurs
     */
    static void commitAndClose(final Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.commit();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * Commits a {@code Connection} then closes it, avoid closing if null
     * and hide any SQLExceptions that occur.
     *
     * @param conn Connection to close.
     */
    static void commitAndCloseQuietly(final Connection conn) {
        try {
            commitAndClose(conn);
        } catch (final SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * Rollback any changes made on the given connection.
     * @param conn Connection to rollback.  A null value is legal.
     * @throws SQLException if a database access error occurs
     */
    static void rollback(final Connection conn) throws SQLException {
        if (conn != null) {
            conn.rollback();
        }
    }

    /**
     * Performs a rollback on the {@code Connection} then closes it,
     * avoid closing if null.
     *
     * @param conn Connection to rollback.  A null value is legal.
     * @throws SQLException if a database access error occurs
     * @since DbUtils 1.1
     */
    static void rollbackAndClose(final Connection conn) throws SQLException {
        if (conn != null) {
            try {
                conn.rollback();
            } finally {
                conn.close();
            }
        }
    }

    /**
     * Performs a rollback on the {@code Connection} then closes it,
     * avoid closing if null and hide any SQLExceptions that occur.
     *
     * @param conn Connection to rollback.  A null value is legal.
     * @since DbUtils 1.1
     */
    static void rollbackAndCloseQuietly(final Connection conn) {
        try {
            rollbackAndClose(conn);
        } catch (final SQLException e) { // NOPMD
            // quiet
        }
    }

    /**
     * Handles rollback in case of exceptions
     * @param con Connection to rollback
     * @return boolean indicating that there was an exception
     */
    static boolean handleExecuteUpdateException(Connection con) {
        System.err.print("Transaction is being rolled back");
        rollbackAndCloseQuietly(con);
        return false;
    }
}