import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.util.*;

public class lab3 {
    private static List<Book3> books = new ArrayList<>();
    private static final String fileName = "books.json";

    public static void main(String[] args) {
        LoadBooks();

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add a new book");
            System.out.println("2. View list of books");
            System.out.println("3. Delete book by name");
            System.out.println("0. Exit program");
            System.out.print("Select an option: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 0:
                    SaveBooks();
                    System.out.println("Program terminated.");
                    return;

                case 1:
                    AddBook();
                    break;

                case 2:
                    Display();
                    break;

                case 3:
                    System.out.print("Enter the book name to delete: ");
                    scanner.nextLine(); // Consume the newline character
                    String name = scanner.nextLine();
                    RemoveBookByName(name);
                    System.out.println("Book " + name + " was deleted.");
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }

    private static void AddBook() {
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Publish: ");
        String publish = scanner.nextLine();

        System.out.print("Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Date: ");
        String date = scanner.nextLine();

        Book3 book = new Book3(isbn, name, publish, genre, date);

        while (true) {
            System.out.print("Add author (Y/N): ");
            String choice = scanner.nextLine();
            if (choice.equalsIgnoreCase("Y")) {
                System.out.print("Author: ");
                String author = scanner.nextLine();
                book.AddAuthor(author);
            } else {
                break;
            }
        }

        books.add(book);
        System.out.println("Book added");
    }

    private static void Display() {
        for (Book3 book : books) {
            System.out.println(book);
        }
    }

    private static void SaveBooks() {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            PrintWriter printWriter = new PrintWriter(fileWriter);

            for (Book3 book : books) {
                printWriter.println(book.getISBN());
                printWriter.println(book.getName());
                printWriter.println(String.join(",", book.getAuthors()));
                printWriter.println(book.getPublish());
                printWriter.println(book.getGenre());
                printWriter.println(book.getDate());
            }

            printWriter.close();
            System.out.println("Data saved successfully to books.json");
        } catch (IOException e) {
            System.err.println("Error saving data to books.json: " + e.getMessage());
        }
    }

    private static void LoadBooks() {
        try {
            File file = new File(fileName);
            if (file.exists()) {
                Scanner scanner = new Scanner(file);
                while (scanner.hasNext()) {
                    String ISBN = scanner.nextLine();
                    String Name = scanner.nextLine();
                    String authors = scanner.nextLine();
                    String Publish = scanner.nextLine();
                    String Genre = scanner.nextLine();
                    String Date = scanner.nextLine();

                    Book3 book = new Book3(ISBN, Name, Publish, Genre, Date);
                    book.AddAuthor(authors);
                    books.add(book);
                }
                scanner.close();
                System.out.println("Data loaded successfully from books.json");
            } else {
                System.out.println("books.json not found. Starting with an empty list.");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Error loading data from books.json: " + e.getMessage() + ". Starting with an empty list.");
        }
    }

    private static void RemoveBookByName(String name) {
        books.removeIf(book -> book.getName().equalsIgnoreCase(name));
    }

}

class Book3 implements Serializable {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;

    public Book3(String ISBN, String Name, String Publish, String Genre, String Date) {
        this.ISBN = ISBN;
        this.Name = Name;
        this.Publish = Publish;
        this.Genre = Genre;
        this.Date = Date;
        this.Authors = new ArrayList<>();
    }

    public void AddAuthor(String author) {Authors.add(author);}

    public String getISBN() {return ISBN;}

    public String getName() {return Name;}

    public List<String> getAuthors() {return Authors;}

    public String getPublish() {return Publish;}

    public String getGenre() {return Genre;}

    public String getDate() {return Date;}

    @Override
    public String toString(){
        return  "Book \n" +
                "ISBN: '" + ISBN + "', "+
                "Name: '" + Name + "', "+
                "Authors: '" + Authors + "', "+
                "Publish: '" + Publish + "', "+
                "Genre: '" + Genre + "', "+
                "Date: '" + Date + "'";
    }
}