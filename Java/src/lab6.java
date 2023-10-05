import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiPredicate;
import java.util.regex.Pattern;

public class lab6 {
    static List<Book6> books = LoadList();
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-auto")){
            Book6 book = new Book6(
                    "00000",
                    "AutoName",
                    "AutoPublish",
                    "Auto",
                    "22-12-2022"
            );
            book.AddAuthor("AutoAuthor");
            books.add(book);
            Display6();
            DisplaySpecificBook();
            SaveBook();
            return;
        }
        while (true){
            System.out.println("Menu:");
            System.out.println("1. Add a new book");
            System.out.println("2. View list of books");
            System.out.println("3. Delete book by name");
            System.out.println("4. Display specific books");
            System.out.println("5. Sort by Name");
            System.out.println("6. Sort by Publish");
            System.out.println("7. Sort by Genre");
            System.out.println("8. Sort by Date");
            System.out.println("0. Exit program");
            System.out.print("Select an option: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 0:
                    SaveBook();
                    System.out.println("Program terminated.");
                    System.exit(0);
                    break;

                case 1:
                    AddBook();
                    break;

                case 2:
                    Display6();
                    break;

                case 3:
                    RemoveBook();
                    break;

                case 4:
                    DisplaySpecificBook();
                    break;

                case 5:
                    SortByName();
                    break;

                case 6:
                    SortByPublish();
                    break;

                case 7:
                    SortByGenre();
                    break;

                case 8:
                    SortByDate();
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
    public static List<Book6> LoadList() {
        List<Book6> books;
        File file = new File("books.json");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object obj = ois.readObject();
                if (obj instanceof List<?>) {
                    books = (List<Book6>) obj;
                    System.out.println("Data loaded successfully from books.json");
                } else {
                    System.err.println("Invalid data format in books.json");
                    books = new ArrayList<>();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data from books.json: " + e.getMessage());
                books = new ArrayList<>();
            }
        } else {
            System.out.println("Starting with an empty list.");
            books = new ArrayList<>();
        }
        return books;
    }
    public static void AddBook(){
        System.out.println();

        Scanner scanner = new Scanner(System.in);

        System.out.print("ISBN: ");
        String ISBN = scanner.nextLine();

        System.out.print("Name: ");
        String Name = scanner.nextLine();

        System.out.print("Publish: ");
        String Publish = scanner.nextLine();

        System.out.print("Genre: ");
        String Genre = scanner.nextLine();

        String Date;
        while (true){
            System.out.print("Date (DD-MM-YYYY): ");
            Date = scanner.nextLine();
            String pattern = "^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$";

            if (Pattern.matches(pattern, Date)) {
                break;
            } else {
                System.out.println("Wrong data format");
            }
        }

        Book6 book = new Book6(ISBN, Name, Publish, Genre, Date);

        while (true){
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
    }
    public static void SaveBook(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.json"))){
            oos.writeObject(books);
            System.out.println("Data successfully saved to file");
        }
        catch (IOException e){
            System.err.println("Error saving data to file: " + e.getMessage());
        }
    }
    public static void Display6(){
        for (Book6 book : books){
            System.out.println(book);
        }
    }
    public static void RemoveBook(){
        System.out.println("Enter book name(s): ");
        Scanner scanner = new Scanner(System.in);
        String BookName = scanner.nextLine();
        books.removeIf(book -> book.getName().equalsIgnoreCase(BookName));
        System.out.println("Book(s) with name " + BookName + " deleted (if found).");
    }
    public static void DisplaySpecificBook(){
        System.out.println("Specific book: ");
        for (Book6 book : books) {
            if (book.HasSpecificAuthor() && book.HasSpecificISBN() && book.HasSpecificAuthor()){
                System.out.println(book);
            }
        }
    }
    public static void SortByName(){
        books.sort(Comparator.comparing(Book6::getName));
    }
    public static void SortByPublish(){
        books.sort(Comparator.comparing(Book6::getPublish));
    }
    public static void SortByGenre(){
        books.sort(Comparator.comparing(Book6::getGenre));
    }
    public static void SortByDate(){
        books.sort(Comparator.comparing(Book6::getDate));
    }
}

class Book6 implements Serializable {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;
    public Book6(String ISBN, String Name, String Publish, String Genre, String Date){
        this.ISBN = ISBN;
        this.Name = Name;
        this.Authors = new ArrayList<>();
        this.Publish = Publish;
        this.Genre = Genre;
        this.Date = Date;
    }
    public String getName() {
        return this.Name;
    }
    public String getPublish() {
        return this.Publish;
    }
    public String getGenre() {
        return this.Genre;
    }
    public String getDate() {
        return this.Date;
    }
    public void AddAuthor(String Author) {
        Authors.add(Author);
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
    public boolean HasSpecificAuthor(){
        return Authors.contains("Й. Й.");
    }
    public boolean HasSpecificISBN(){
        String pattern = ".*9\\d9\\d9.*";
        return Pattern.matches(pattern, ISBN);
    }
    public boolean HasSpecificYear() {
        int currentYear = java.time.Year.now().getValue();
        String pattern = String.format(
                "^\\d{2}-\\d{2}-%d|\\d{2}-\\d{2}-%d|\\d{2}-\\d{2}-%d|\\d{2}-\\d{2}-%d|\\d{2}-\\d{2}-%d$",
                currentYear - 4, currentYear - 3, currentYear - 2, currentYear - 1, currentYear
        );
        return Pattern.matches(pattern, Date);
    }
}