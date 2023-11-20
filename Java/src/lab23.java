import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class Book {
    private String ISBN;
    private String Name;
    private List<String> Authors;
    private String Publish;
    private String Genre;
    private String Date;
    public Book(String ISBN, String Name, String Publish, String Genre, String Date){
        this.ISBN = ISBN;
        this.Name = Name;
        this.Authors = new ArrayList<>();
        this.Publish = Publish;
        this.Genre = Genre;
        this.Date = Date;
    }
    public void AddAuthor(String Author) {
        Authors.add(Author);
    }
    public String getISBN(){
        return this.ISBN;
    }
    public String getName(){
        return this.Name;
    }
    public String getPublish(){
        return this.Publish;
    }
    public String getGenre(){
        return this.Genre;
    }
    public String getDate(){
        return this.Date;
    }
}

class BookContainer {
    private List<Book> books;
    public BookContainer(List<Book> books)
    {
        this.books = books;
    }

    public synchronized List<Book> SortByName() {
        List<Book> sortedRoutes = new ArrayList<>(books);
        Collections.sort(sortedRoutes, (r1, r2) -> r1.getName().compareTo(r2.getName()));
        return sortedRoutes;
    }

    public synchronized List<Book> SortByPublish() {
        List<Book> sortedRoutes = new ArrayList<>(books);
        Collections.sort(sortedRoutes, (r1, r2) -> r1.getPublish().compareTo(r2.getPublish()));
        return sortedRoutes;
    }

    public synchronized List<Book> SortByGenre() {
        List<Book> sortedRoutes = new ArrayList<>(books);
        Collections.sort(sortedRoutes, (r1, r2) -> r1.getGenre().compareTo(r2.getGenre()));
        return sortedRoutes;
    }

    public synchronized List<Book> SortByDate() {
        List<Book> sortedRoutes = new ArrayList<>(books);
        Collections.sort(sortedRoutes, (r1, r2) -> r1.getDate().compareTo(r2.getDate()));
        return sortedRoutes;
    }
}
public class lab23 {
    public static void main(String[] args) {
        List<Book> books = new ArrayList<>();

        books.add(new Book("978142890", "The Mysterious Island", "John Smith", "AdventureInc", "2022-08-15"));
        books.add(new Book("123456789", "Journey to the Center of the Earth", "Mystery Novels Publishing", "Mystery", "2023-04-10"));
        books.add(new Book("567890123", "The Code Breaker", "Exploration Publications", "Adventure", "2021-12-05"));
        books.add(new Book("987654321", "To Kill a Mockingbird", "Simon & Schuster", "Non-fiction", "2023-02-20"));
        books.add(new Book("111223344", "The Adventure Chronicles", "J.B. Lippincott & Co.", "Fiction", "2021-07-11"));

        BookContainer container = new BookContainer(books);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        List<Book> resultByName = new ArrayList<>();
        List<Book> resultByPublish = new ArrayList<>();
        List<Book> resultByGenre = new ArrayList();
        List<Book> resultByDate = new ArrayList();

        executorService.execute(() -> {
            resultByName.addAll(container.SortByName());
        });

        executorService.execute(() -> {
            resultByPublish.addAll(container.SortByPublish());
        });

        executorService.execute(() -> {
            resultByGenre.addAll(container.SortByGenre());
        });

        executorService.execute(() -> {
            resultByDate.addAll(container.SortByDate());
        });

        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long startTime = System.nanoTime();
        List<Book> sequentialResultByName = container.SortByName();
        List<Book> sequentialResultByPublish = container.SortByPublish();
        List<Book> sequentialResultByGenre = container.SortByGenre();
        List<Book> sequentialResultByDate = container.SortByDate();
        long endTime = System.nanoTime();
        long sequentialTime = endTime - startTime;

        System.out.println("Sequential processing time: " + sequentialTime + " nanosecond");

        System.out.println("\nSort results by name (in parallel):");
        for (Book book : resultByName) {
            System.out.println(book.getName());
        }

        System.out.println("\nSort results by publish (in parallel):");
        for (Book book : resultByPublish) {
            System.out.println(book.getPublish());
        }

        System.out.println("\nSort results by genre (in parallel):");
        for (Book book : resultByGenre) {
            System.out.println(book.getGenre());
        }

        System.out.println("\nSort results by date (in parallel):");
        for (Book book : resultByDate) {
            System.out.println(book.getDate());
        }
    }
}