package roco2a;

import java.util.Scanner;

public class Roco2a {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Config conf = new Config();
        
        while (true) {
            System.out.println("\nWELCOME TO LIBRARY");
            System.out.println("Actions");
            System.out.println("1. Add Library Record");
            System.out.println("2. Update Library Record");
            System.out.println("3. Delete Library Record");
            System.out.println("4. View Library Records");
            System.out.println("5. Check Book Availability");
            System.out.println("6. EXIT");
            System.out.print("Choose Action: ");
            int action = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (action) {
                case 1:
                    // Add Library Record
                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Is Available (true/false): ");
                    boolean availability = sc.nextBoolean();
                    System.out.print("Enter Published Year: ");
                    int publishedYear = sc.nextInt();
                    
                    String sqlInsert = "INSERT INTO books (book_title, author, isbn, availability, published_year) VALUES (?, ?, ?, ?, ?)";
                    conf.addRecord(sqlInsert, title, author, isbn, availability, publishedYear);
                    break;

                case 2:
                    System.out.print("Enter ISBN of the book to update: ");
                    String updateIsbn = sc.nextLine();
                    System.out.print("Is Available (true/false): ");
                    boolean updatedAvailability = sc.nextBoolean();
                    
                    String sqlUpdate = "UPDATE books SET availability = ? WHERE isbn = ?";
                    conf.updateRecord(sqlUpdate, updatedAvailability, updateIsbn);
                    break;

                case 3:
                    System.out.print("Enter ISBN of the book to delete: ");
                    String deleteIsbn = sc.nextLine();
                    String sqlDelete = "DELETE FROM books WHERE isbn = ?";
                    conf.deleteRecord(sqlDelete, deleteIsbn);
                    break;

                case 4:
                    String sqlSelect = "SELECT * FROM books";
                    String[] headers = {"Book Title", "Author", "ISBN", "Availability", "Published Year"};
                    String[] columnNames = {"book_title", "author", "isbn", "availability", "published_year"};
                    conf.viewRecords(sqlSelect, headers, columnNames);
                    break;

                case 5:
                    System.out.print("Enter ISBN of the book to check availability: ");
                    String checkIsbn = sc.nextLine();
                    boolean isAvailable = conf.checkBookAvailability(checkIsbn);
                    if (isAvailable) {
                        System.out.println("The book is available for use.");
                    } else {
                        System.out.println("The book is not available.");
                    }
                    break;

                case 6:
                    // Exit
                    System.out.println("Exiting the program. Goodbye!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please choose again.");
            }
        }
    }
}
