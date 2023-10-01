using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Text.Json;
using System.Text.RegularExpressions;
using System.Text.Unicode;
using System.Threading.Tasks;

namespace CSSSS
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
        public bool HasSpecificAuthor()
        {
            return Authors.Contains("Й. Й.");
        }
        public bool HasSpecificISBN()
        {
            string pattern = @".9\d9\d9.";
            return Regex.IsMatch(ISBN, pattern);
        }
        public bool HasSpecificYear()
        {
            int currentYear = DateTime.Now.Year;
            string pattern = $@"^\d{{2}}-\d{{2}}-{currentYear - 4}|\d{{2}}-\d{{2}}-{currentYear - 3}|\d{{2}}-\d{{2}}-{currentYear - 2}|\d{{2}}-\d{{2}}-{currentYear - 1}|\d{{2}}-\d{{2}}-{currentYear}$";
            return Regex.IsMatch(Date, pattern);
        }

        public override string ToString()
        {
            return $"Book \nISBN: '{ISBN}',Name: '{Name}', Authors: '{string.Join(", ", Authors)}',Publish: '{Publish}', Genre: '{Genre}', Date:'{Date}'";
        }
    }

    public class MyLinkedList<T> : List<T>
    {
        public void RemoveIf(Predicate<T> predicate)
        {
            this.RemoveAll(predicate);
        }
    }

    internal class lab6
    {
        private static MyLinkedList<Book> books = new MyLinkedList<Book>();
        private const string FileName = "books.json";

        static void Main(string[] args)
        {
            LoadBooks();

            if (args.Length > 0 && args[0].Equals("-auto", StringComparison.OrdinalIgnoreCase))
            {
                books.Add(new Book
                {
                    ISBN = "00000",
                    Name = "Auto",
                    Authors = new List<string> { "Auto" },
                    Publish = "Auto",
                    Genre = "Auto",
                    Date = "22-12-2022"
                });
                Display();
                SaveBooks();
                Console.WriteLine("Program terminated.");
                return;
            }

            while (true)
            {
                Console.WriteLine("Menu:");
                Console.WriteLine("1. Add a new book");
                Console.WriteLine("2. View list of books");
                Console.WriteLine("3. Delete book by name");
                Console.WriteLine("4. Find specific book");
                Console.WriteLine("5. Search book by keyword");
                Console.WriteLine("6. Sort by name");
                Console.WriteLine("7. Sort by publish");
                Console.WriteLine("8. Sort by genre");
                Console.WriteLine("9. Sort by date");
                Console.WriteLine("0. Exit program");
                Console.Write("Select an option: ");

                int choice = int.Parse(Console.ReadLine());
                Console.WriteLine();

                switch (choice)
                {
                    case 0:
                        SaveBooks();
                        Console.WriteLine("Program terminated.");
                        return;

                    case 1:
                        AddBook();
                        break;

                    case 2:
                        Display();
                        break;

                    case 3:
                        Console.Write("Enter the book name to delete: ");
                        string Name = Console.ReadLine();
                        books.RemoveIf(s => s.Name.Equals(Name, StringComparison.OrdinalIgnoreCase));
                        Console.WriteLine($"Book {Name} was deleted.");
                        break;

                    case 4:
                        DisplaySpecificBook();
                        break;

                    case 5:
                        SearchBook();
                        break;

                    case 6:
                        SortByName();
                        break;
                    
                    case 7:
                        SortByPublish();
                        break;

                    case 8:
                        SortByGenre();
                        break;

                    case 9:
                        SortByDate();
                        break;

                    default:
                        Console.WriteLine("Invalid choice");
                        break;
                }
            }
        }
        private static void LoadBooks()
        {
            if (File.Exists(FileName))
            {
                try
                {
                    var json = File.ReadAllText(FileName, Encoding.UTF8);
                    books = JsonSerializer.Deserialize<MyLinkedList<Book>>(json);
                    Console.WriteLine("Data loaded successfully from books.json");
                }
                catch (Exception e)
                {
                    Console.WriteLine($"Error loading data from books.json: {e.Message}. Starting with an empty list.");
                }
            }
            else
            {
                Console.WriteLine("books.json not found. Starting with an empty list.");
            }
        }
        private static void AddBook()
        {
            Console.WriteLine();

            Console.Write("ISBN: ");
            var isbn = Console.ReadLine();

            Console.Write("Name: ");
            var name = Console.ReadLine();

            Console.Write("Publish: ");
            var publish = Console.ReadLine();

            Console.Write("Genre: ");
            var genre = Console.ReadLine();

            string date;

            while (true)
            {
                Console.Write("Date(DD-MM-YYYY): ");
                date = Console.ReadLine();
                string pattern = @"^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\d{4}$";
                if (Regex.IsMatch(date, pattern))
                    break;
                else
                    Console.WriteLine("Wrong data format");
            }

            var book = new Book
            {
                ISBN = isbn,
                Name = name,
                Publish = publish,
                Genre = genre,
                Date = date
            };

            while (true)
            {
                Console.WriteLine("Add author (Y/N): ");
                if (Console.ReadLine().Equals("Y", StringComparison.OrdinalIgnoreCase))
                {
                    Console.Write("Author: ");
                    book.AddAuthor(Console.ReadLine());
                }
                else
                    break;
            }

            books.Add(book);
            Console.WriteLine("Book added");
        }
        private static void Display()
        {
            foreach (var book in books)
            {
                Console.WriteLine(book);
            }
        }

        private static void DisplaySpecificBook()
        {
            foreach (Book book in books)
            {
                if (book.HasSpecificAuthor() && book.HasSpecificISBN() && book.HasSpecificYear())
                {
                    Console.WriteLine(book);
                }
            }
        }
        private static void SaveBooks()
        {
            var json = JsonSerializer.Serialize(books, new JsonSerializerOptions { WriteIndented = true, Encoder = System.Text.Encodings.Web.JavaScriptEncoder.Create(UnicodeRanges.All) });
            File.WriteAllText(FileName, json, Encoding.UTF8);
        }
        private static void SearchBook()
        {
            Console.Write("Enter keyword to search: ");
            var word = Console.ReadLine().ToLower();

            var filtered = books.Where(book => book.ISBN.ToLower().Contains(word)
                                                || book.Name.ToLower().Contains(word)
                                                || string.Join(", ", book.Authors).ToLower().Contains(word)
                                                || book.Publish.ToLower().Contains(word)
                                                || book.Genre.ToLower().Contains(word)
                                                || book.Date.ToLower().Contains(word));

            foreach (var book in filtered)
            {
                Console.WriteLine(book);
            }
        }
        private static void SortByName()
        {
            books.Sort((book1, book2) => book1.Name.CompareTo(book2.Name));
        }
        private static void SortByPublish()
        {
            books.Sort((book1, book2) => book1.Publish.CompareTo(book2.Publish));
        }
        private static void SortByGenre()
        {
            books.Sort((book1, book2) => book1.Genre.CompareTo(book2.Genre));
        }
        private static void SortByDate()
        {
            books.Sort((book1, book2) => book1.Date.CompareTo(book2.Date));
        }
    }
}