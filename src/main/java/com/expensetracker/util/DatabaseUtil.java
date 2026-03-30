package com.expensetracker.util;

import java.sql.*;

public class DatabaseUtil {

    // Database file will be created in the project root folder
    private static final String DB_URL = "jdbc:sqlite:expenses.db";

    /**
     * Returns a connection to the SQLite database
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    /**
     * Creates the expenses table if it doesn't exist
     */
    public static void initializeDatabase() {
        String sql = """
            CREATE TABLE IF NOT EXISTS expenses (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT NOT NULL,
                category TEXT NOT NULL,
                amount REAL NOT NULL,
                description TEXT
            );
            """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("✅ Database initialized successfully. Table 'expenses' is ready.");

        } catch (SQLException e) {
            System.err.println("❌ Database initialization failed:");
            e.printStackTrace();
        }
    }
}