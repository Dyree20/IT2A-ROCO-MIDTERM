package roco2a;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Config {
    
    public static Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("org.sqlite.JDBC");
            con = DriverManager.getConnection("jdbc:sqlite:Library.db"); // Ensure this path is correct
            System.out.println("Connection Successful");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return con;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS books (" +
                     "library_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                     "book_title TEXT NOT NULL, " +
                     "author TEXT NOT NULL, " +
                     "isbn TEXT NOT NULL UNIQUE, " +
                     "availability BOOLEAN NOT NULL, " +
                     "published_year INTEGER NOT NULL)";
        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot create table because connection is null.");
                return; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.executeUpdate();
                System.out.println("Table created successfully (if it did not exist).");
            }
        } catch (SQLException e) {
            System.out.println("Error creating table: " + e.getMessage());
        }
    }

    public void addRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot add record because connection is null.");
                return; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    setPreparedStatementValue(pstmt, i + 1, values[i]);
                }
                pstmt.executeUpdate();
                System.out.println("Record added successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error adding record: " + e.getMessage());
        }
    }

    public void viewRecords(String sqlQuery, String[] columnHeaders, String[] columnNames) {
        if (columnHeaders.length != columnNames.length) {
            System.out.println("Error: Mismatch between column headers and column names.");
            return;
        }

        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot view records because connection is null.");
                return; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sqlQuery);
                 ResultSet rs = pstmt.executeQuery()) {

                StringBuilder headerLine = new StringBuilder();
                headerLine.append("--------------------------------------------------------------------------------\n| ");
                for (String header : columnHeaders) {
                    headerLine.append(String.format("%-20s | ", header));
                }
                headerLine.append("\n--------------------------------------------------------------------------------");

                System.out.println(headerLine.toString());

                while (rs.next()) {
                    StringBuilder row = new StringBuilder("| ");
                    for (String colName : columnNames) {
                        String value = rs.getString(colName);
                        row.append(String.format("%-20s | ", value != null ? value : ""));
                    }
                    System.out.println(row.toString());
                }
                System.out.println("--------------------------------------------------------------------------------");
            }
        } catch (SQLException e) {
            System.out.println("Error retrieving records: " + e.getMessage());
        }
    }

    public void updateRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot update record because connection is null.");
                return; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    setPreparedStatementValue(pstmt, i + 1, values[i]);
                }
                pstmt.executeUpdate();
                System.out.println("Record updated successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error updating record: " + e.getMessage());
        }
    }

    public void deleteRecord(String sql, Object... values) {
        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot delete record because connection is null.");
                return; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < values.length; i++) {
                    setPreparedStatementValue(pstmt, i + 1, values[i]);
                }
                pstmt.executeUpdate();
                System.out.println("Record deleted successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting record: " + e.getMessage());
        }
    }

    public boolean checkBookAvailability(String isbn) {
        String sql = "SELECT availability FROM books WHERE isbn = ?";
        try (Connection conn = connectDB()) {
            if (conn == null) {
                System.out.println("Cannot check availability because connection is null.");
                return false; // Abort if connection fails
            }
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, isbn);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    return rs.getBoolean("availability");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking availability: " + e.getMessage());
        }
        return false; // Return false if not found or an error occurs
    }

    private void setPreparedStatementValue(PreparedStatement pstmt, int index, Object value) throws SQLException {
        if (value instanceof Integer) {
            pstmt.setInt(index, (Integer) value);
        } else if (value instanceof Double) {
            pstmt.setDouble(index, (Double) value);
        } else if (value instanceof Float) {
            pstmt.setFloat(index, (Float) value);
        } else if (value instanceof Long) {
            pstmt.setLong(index, (Long) value);
        } else if (value instanceof Boolean) {
            pstmt.setBoolean(index, (Boolean) value);
        } else if (value instanceof java.util.Date) {
            pstmt.setDate(index, new java.sql.Date(((java.util.Date) value).getTime()));
        } else if (value instanceof java.sql.Date) {
            pstmt.setDate(index, (java.sql.Date) value);
        } else if (value instanceof java.sql.Timestamp) {
            pstmt.setTimestamp(index, (java.sql.Timestamp) value);
        } else {
            pstmt.setString(index, value.toString());
        }
    }

    public static void main(String[] args) {
        Config conf = new Config();
        conf.createTable(); // Ensure the table exists

        // Example usage: Add a book
        String sqlInsert = "INSERT INTO books (book_title, author, isbn, availability, published_year) VALUES (?, ?, ?, ?, ?)";
        conf.addRecord(sqlInsert, "The Great Gatsby", "F. Scott Fitzgerald", "9780743273565", true, 1925);

        // View records
        String sqlSelect = "SELECT * FROM books";
        String[] headers = {"Book Title", "Author", "ISBN", "Availability", "Published Year"};
        String[] columnNames = {"book_title", "author", "isbn", "availability", "published_year"};
        conf.viewRecords(sqlSelect, headers, columnNames);
    }
}
