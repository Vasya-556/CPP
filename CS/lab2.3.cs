using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS23
{
    public class Book
    {
        public string ISBN { get; set; }
        public string Name { get; set; }
        public List<string> Authors { get; set; } = new List<string>();
        public string Publish { get; set; }
        public string Genre { get; set; }
        public string Date { get; set; }

        public void AddAuthor(string Author)
        {
            Authors.Add(Author);
        }
        public override string ToString()
        {
            return $"Book \nISBN: '{ISBN}',Name: '{Name}', Authors: '{string.Join(", ", Authors)}',Publish: '{Publish}', Genre: '{Genre}', Date:'{Date}'";
        }
    }

    class BookContainer
    {
        private List<Book> books;

        public BookContainer(List<Book> books)
        {
            this.books = books;
        }

        public List<Book> SortByName()
        {
            List<Book> sortedBooks = new List<Book>(books);
            sortedBooks.Sort((b1, b2) => string.Compare(b1.Name, b2.Name, StringComparison.Ordinal));
            return sortedBooks;
        }

        public List<Book> SortByPublish()
        {
            List<Book> sortedBooks = new List<Book>(books);
            sortedBooks.Sort((b1, b2) => string.Compare(b1.Publish, b2.Publish, StringComparison.Ordinal));
            return sortedBooks;
        }

        public List<Book> SortByGenre()
        {
            List<Book> sortedBooks = new List<Book>(books);
            sortedBooks.Sort((b1, b2) => string.Compare(b1.Genre, b2.Genre, StringComparison.Ordinal));
            return sortedBooks;
        }

        public List<Book> SortByDate()
        {
            List<Book> sortedBooks = new List<Book>(books);
            sortedBooks.Sort((b1, b2) => DateTime.Compare(DateTime.Parse(b1.Date), DateTime.Parse(b2.Date)));
            return sortedBooks;
        }
    }

    internal class lab23
    {
        static void Main(string[] args)
        {
            List<Book> books = new List<Book>();
            books.Add(new Book { ISBN = "978142890", Name = "The Adventure Chronicles", Authors = new List<string> { "John Smith" }, Publish = "Adventure Books Inc", Genre = "Adventure", Date = "2022-08-15" });
            books.Add(new Book { ISBN = "123456789", Name = "The Mysterious Island", Authors = new List<string> { "Sarah Brown", "Alex Johnson" }, Publish = "Mystery Novels Publishing", Genre = "Mystery", Date = "2023-04-10" });
            books.Add(new Book { ISBN = "567890123", Name = "Journey to the Center of the Earth", Authors = new List<string> { "George Orwell" }, Publish = "Exploration Publications", Genre = "Adventure", Date = "2021-12-05" });
            books.Add(new Book { ISBN = "987654321", Name = "The Code Breaker", Authors = new List<string> { "Walter Isaacson" }, Publish = "Simon & Schuster", Genre = "Non-fiction", Date = "2023-02-20" });
            books.Add(new Book { ISBN = "111223344", Name = "To Kill a Mockingbird", Authors = new List<string> { "Harper Lee" }, Publish = "J.B. Lippincott & Co.", Genre = "Fiction", Date = "2021-07-11" });

            BookContainer bookContainer = new BookContainer(books);

            // Створення завдань для паралельної обробки
            var tasks = new List<Task<List<Book>>>();
            tasks.Add(Task.Run(() => bookContainer.SortByName()));
            tasks.Add(Task.Run(() => bookContainer.SortByPublish()));
            tasks.Add(Task.Run(() => bookContainer.SortByGenre()));
            tasks.Add(Task.Run(() => bookContainer.SortByDate()));

            // Очікування завершення всіх завдань
            Task.WhenAll(tasks).Wait();

            // Отримання результатів паралельної обробки
            List<Book> resultByName = tasks[0].Result;
            List<Book> resultByPublish = tasks[1].Result;
            List<Book> resultByGenre = tasks[2].Result;
            List<Book> resultByDate = tasks[3].Result;

            // Порівняння часу послідовної обробки
            var stopwatch = new System.Diagnostics.Stopwatch();
            stopwatch.Start();
            List<Book> sequentialResultByName = bookContainer.SortByName();
            List<Book> sequentialResultByPublish = bookContainer.SortByPublish();
            List<Book> sequentialResultByGenre = bookContainer.SortByGenre();
            List<Book> sequentialResultByDate = bookContainer.SortByDate();
            stopwatch.Stop();
            long sequentialTime = stopwatch.ElapsedTicks;

            Console.WriteLine("Sequential processing time: " + sequentialTime + " ticks");

            // Обробка і виведення результатів паралельної обробки
            Console.WriteLine("\nSort results by name (in parallel):");
            foreach (Book book in resultByName)
            {
                Console.WriteLine(book.Name);
            }

            Console.WriteLine("\nSort results by publish (in parallel):");
            foreach (Book book in resultByPublish)
            {
                Console.WriteLine(book.Publish);
            }

            Console.WriteLine("\nSort results by genres (in parallel):");
            foreach (Book book in resultByGenre)
            {
                Console.WriteLine(book.Genre);
            }

            Console.WriteLine("\nSort results by date (in parallel):");
            foreach (Book book in resultByDate)
            {
                Console.WriteLine(book.Date);
            }

            Console.ReadKey();
        }
    }
}
