import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class lab2 {
    public static void main(String[] args) {
        Library<Book> LibraryDirectory = new Library<>();

        Book book1 = new Book("97814890", "The Adventure Chronicles", "Adventure Books Inc", "Adventure", "2022-08-15");
        book1.AddAuthor("John Smith");

        Book book2 = new Book("123456789", "The Mysterious Island", "Mystery Novels Publishing", "Mystery", "2023-04-10");
        book2.AddAuthor("Sarah Brown");
        book2.AddAuthor("Alex Johnson");

        Book book3 = new Book("567890123", "Journey to the Center of the Earth", "Exploration Publications", "Adventure", "2021-12-05");
        book3.AddAuthor("George Orwell");

        LibraryDirectory.AddBook(book1);
        LibraryDirectory.AddBook(book2);
        LibraryDirectory.AddBook(book3);

        System.out.println("All books:");
        LibraryDirectory.Display();

        LibraryDirectory.SortByName();
        System.out.println("\nBooks sorted by Name:");
        LibraryDirectory.Display();

        LibraryDirectory.SortByPublish();
        System.out.println("\nBooks sorted by Publish:");
        LibraryDirectory.Display();

        LibraryDirectory.SortByGenre();
        System.out.println("\nBooks sorted by Genre:");
        LibraryDirectory.Display();

        LibraryDirectory.SortByDate();
        System.out.println("\nBooks sorted by Date:");
        LibraryDirectory.Display();

    }
}

class Book {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;
    public Book(String ISBN,String Name,String Publish,String Genre,String Date) {
        this.ISBN = ISBN;
        this.Name = Name;
        this.Publish = Publish;
        this.Genre = Genre;
        this.Date = Date;
        this.Authors = new ArrayList<String>();
    }
    public String getName(){return Name;}
    public String getPublish(){return Publish;}
    public String getGenre(){return Genre;}
    public String getDate(){return Date;}
    public void AddAuthor(String Author){
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
}

class Library<T>{
    private List<T> Books;
    public Library() { Books = new ArrayList<>();}
    public void AddBook(T book){ Books.add(book);}
    public void Display(){
        for (T book: Books){
            System.out.println(book.toString());
        }
    }
    public void SortByName(){
        Collections.sort(Books, Comparator.comparing(book -> ((Book) book).getName()));
    }
    public void SortByPublish(){
        Collections.sort(Books, Comparator.comparing(book -> ((Book) book).getPublish()));
    }
    public void SortByGenre(){
        Collections.sort(Books, Comparator.comparing(book -> ((Book) book).getGenre()));
    }
    public void SortByDate(){
        Collections.sort(Books, Comparator.comparing(book -> ((Book) book).getDate()));
    }
}