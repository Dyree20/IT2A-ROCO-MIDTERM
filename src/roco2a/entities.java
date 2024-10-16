
package roco2a;

import java.sql.PreparedStatement;
import java.sql.SQLException;


public class entities {
    
    private void createTable() {
        // Creating vehicles table
        try (PreparedStatement statement = Config.PreparedStatement(
                "CREATE TABLE IF NOT EXISTS Library (library_id VARCHAR(255) PRIMARY KEY, book_title VARCHAR(255), author VARCHAR(255));")) {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating vehicles table: " + e.getMessage());
        }
    
    
}
    
    try (PreparedStatement statement = Config.PreparedStatement(
                "CREATE TABLE IF NOT EXISTS Library (library_id INTEGER PRIMARY KEY AUTOINCREMENT, book_title VARCHAR(255), author VARCHAR(255), isb VARCHAR(255), availability VARCHAR(255), published_year VARCHAR(255);"); {
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error creating vehicle_logs table: " + e.getMessage());
        }
    
}