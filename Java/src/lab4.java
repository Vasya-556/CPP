import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class lab4 {
    private static List<Book4> books = new ArrayList<>();
    private static final String fileName = "books.json";
    public static void main(String[] args) {
        LoadBooks();

        if (args.length > 0 && args[0].equalsIgnoreCase("-auto")) {
            Book4 autoBook = new Book4(
                    "00000",
                    "Auto",
                    "Auto",
                    "Auto",
                    "22-12-2022"
            );
            autoBook.AddAuthor("Auto");

            books.add(autoBook);
            Display();
            SaveBooks();
            System.out.println("Program terminated.");
            return;
        }

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
                    scanner.nextLine();
                    String name = scanner.nextLine();
                    books.removeIf(book -> book.getName().equalsIgnoreCase(name));
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

        Book4 book = new Book4(isbn, name, publish, genre, date);

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
        for (Book4 book : books) {
            System.out.println(book);
        }
    }

    private static void SaveBooks() {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(fileName))) {
            outputStream.writeObject(books);
            System.out.println("Data saved successfully to books.json");
        } catch (IOException e) {
            System.err.println("Error saving data to books.json: " + e.getMessage());
        }
    }

    private static void LoadBooks() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(fileName))) {
            Object obj = inputStream.readObject();
            if (obj instanceof List<?>) {
                books = (List<Book4>) obj;
                System.out.println("Data loaded successfully from books.json");
            } else {
                System.out.println("books.json does not contain a valid list of books. Starting with an empty list.");
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data from books.json: " + e.getMessage() + ". Starting with an empty list.");
        }
    }
}

class MyLinkedList<T> extends ArrayList<T> {
    public void RemoveIf(Predicate<T> predicate) {
        removeIf(predicate);
    }
}

class Book4 implements Serializable {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;

    public Book4(String ISBN, String Name, String Publish, String Genre, String Date) {
        this.ISBN = ISBN;
        this.Name = Name;
        this.Publish = Publish;
        this.Genre = Genre;
        this.Date = Date;
        this.Authors = new ArrayList<>();
    }
    public void AddAuthor(String author) {
        Authors.add(author);
    }
    public String getName() {
        return Name;
    }
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