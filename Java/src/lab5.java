import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

public class lab5 {
    static MyLinkedList5<Book5> books = LoadList();
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("-auto")){
            Book5 book = new Book5(
                    "00000",
                    "AutoName",
                    "AutoPublish",
                    "Auto",
                    "22-12-2022"
            );
            book.AddAuthor("AutoAuthor");
            books.add(book);
            Display5.PrintList(books);
            DisplaySpecificBook();
            SaveBook();
            return;
        }
        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add a new book");
            System.out.println("2. View list of books");
            System.out.println("3. Delete book by name");
            System.out.println("4. Display specific books");
            System.out.println("0. Exit program");
            System.out.print("Select an option: ");

            Scanner scanner = new Scanner(System.in);
            int choice = scanner.nextInt();
            System.out.println();

            switch (choice) {
                case 0:
                    SaveBook();
                    System.out.println("Program terminated.");
                    break;

                case 1:
                    AddBook();
                    break;

                case 2:
                    Display5.PrintList(books);
                    break;

                case 3:
                    RemoveBook();
                    break;

                case 4:
                    DisplaySpecificBook();
                    break;

                default:
                    System.out.println("Invalid choice");
                    break;
            }
        }
    }
    public static MyLinkedList5<Book5> LoadList() {
        MyLinkedList5<Book5> books;
        File file = new File("books.json");
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                Object object = ois.readObject();
                if (object instanceof MyLinkedList5<?>) {
                    books = (MyLinkedList5<Book5>) object;
                    System.out.println("Data loaded successfully from books.json");
                } else {
                    System.err.println("books.json does not contain a valid list of books.");
                    books = new MyLinkedList5<>();
                }
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading data from books.json: " + e.getMessage());
                books = new MyLinkedList5<>();
            }
        } else {
            System.out.println("Starting with an empty list.");
            books = new MyLinkedList5<>();
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

        Book5 book = new Book5(ISBN, Name, Publish, Genre, Date);

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
    public static void RemoveBook(){
        System.out.print("Enter book name(s): ");
        Scanner scanner = new Scanner(System.in);
        String BookName = scanner.nextLine();
        books.removeIf(book -> book.getName().equalsIgnoreCase(BookName));
        System.out.println("Book(s) with name " + BookName + " deleted (if found).");
    }
    public static void SaveBook(){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("books.json"))){
            oos.writeObject(books);
            System.out.println();
        }
        catch (IOException e){
            System.err.println("Error saving data to file: " + e.getMessage());
        }
        System.out.println("Program terminated");
        System.exit(0);
    }
    public static void DisplaySpecificBook(){
        System.out.println("Specific book: ");
        for (Book5 book : books) {
            if (book.HasSpecificAuthor() && book.HasSpecificISBN() && book.HasSpecificAuthor()){
                System.out.println(book);
            }
        }
    }
}

class Node5<T> implements Serializable {
    T data;
    Node5<T> next;

    Node5(T data) {
        this.data = data;
        this.next = null;
    }
}

class MyLinkedList5<T> implements Iterable<T>, Serializable {
    private Node5<T> head = null;

    public void add(T item) {
        Node5<T> newNode = new Node5<>(item);
        if (head == null) {
            head = newNode;
        } else {
            Node5<T> temp = head;
            while (temp.next != null) {
                temp = temp.next;
            }
            temp.next = newNode;
        }
    }

    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Node5<T> current = head;

            public boolean hasNext() {
                return current != null;
            }

            public T next() {
                T data = current.data;
                current = current.next;
                return data;
            }
        };
    }

    public void removeIf(java.util.function.Predicate<T> predicate) {
        if (head == null) return;

        while (head != null && predicate.test(head.data)) {
            head = head.next;
        }

        Node5<T> current = head;
        while (current != null && current.next != null) {
            if (predicate.test(current.next.data)) {
                current.next = current.next.next;
            } else {
                current = current.next;
            }
        }
    }
}

class Display5 {
    public static <T> void PrintList(MyLinkedList5<T> list){
        for (T item : list){
            System.out.println(item);
        }
    }
}

class Book5 implements Serializable {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;
    public Book5(String ISBN, String Name, String Publish, String Genre, String Date){
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