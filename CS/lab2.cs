using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS
{
    public class Book
    {
        public int ISBN { get; }
        public string Name { get; }
        public List<string> Authors { get; } 
        public string Publish {  get; }
        public string Genre {  get; }
        public string Date { get; }

        public Book(int ISBN, string Name, string Publish, string Genre, string Date)
        {
            this.ISBN = ISBN;
            this.Name = Name;
            this.Authors = new List<string>();
            this.Publish = Publish;
            this.Genre = Genre;
            this.Date = Date;
        }    

        public void AddAuthor(string Author)
        {
            Authors.Add(Author);
        }

        public override string ToString()
        {
            return $"Book \nISBN: '{ISBN}',Name: '{Name}', Authors: '{string.Join(", ", Authors)}',Publish: '{Publish}', Genre: '{Genre}', Date:'{Date}'";
        }
    }
    public class Library<T>
    {
        private List<T> Books = new List<T>();
        public void AddBook(T Book)
        {
            Books.Add(Book);
        }
        public void Display()
        {
            foreach (var Book in Books)
            {
                Console.WriteLine(Book.ToString());
            }
        }
        public void SortByName()
        {
            Books.Sort((Book1, Book2) => (Book1 as Book).Name.CompareTo((Book2 as Book).Name));
        }
        public void SortByPublish()
        {
            Books.Sort((Book1, Book2) => (Book1 as Book).Publish.CompareTo((Book2 as Book).Publish));
        }
        public void SortByGenre()
        {
            Books.Sort((Book1, Book2) => (Book1 as Book).Genre.CompareTo((Book2 as Book).Genre));
        }
        public void SortByDate()
        {
            Books.Sort((Book1, Book2) => (Book1 as Book).Date.CompareTo((Book2 as Book).Date));
        }
    }
    internal class lab2
    {
        static void Main(string[] args)
        {
            var LibraryDirectory = new Library<Book>();

            var Book1 = new Book(97814890, "The Adventure Chronicles", "Adventure Books Inc", "Adventure", "2022-08-15");
            Book1.AddAuthor("John Smith");

            var Book2 = new Book(123456789, "The Mysterious Island", "Mystery Novels Publishing", "Mystery", "2023-04-10");
            Book2.AddAuthor("Sarah Brown");
            Book2.AddAuthor("Alex Johnson");

            var Book3 = new Book(567890123, "Journey to the Center of the Earth", "Exploration Publications", "Adventure", "2021-12-05");
            Book3.AddAuthor("George Orwell");

            LibraryDirectory.AddBook(Book1);
            LibraryDirectory.AddBook(Book2);
            LibraryDirectory.AddBook(Book3);

            Console.WriteLine("All books:");
            LibraryDirectory.Display();

            LibraryDirectory.SortByName();
            Console.WriteLine("\nBooks sorted by Name:");
            LibraryDirectory.Display();

            LibraryDirectory.SortByPublish();
            Console.WriteLine("\nBooks sorted by Publish:");
            LibraryDirectory.Display();

            LibraryDirectory.SortByGenre();
            Console.WriteLine("\nBooks sorted by Genre:");
            LibraryDirectory.Display();

            LibraryDirectory.SortByDate();
            Console.WriteLine("\nBooks sorted by Date:");
            LibraryDirectory.Display();

            Console.ReadKey();
        }

    }
}
