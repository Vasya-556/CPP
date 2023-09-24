using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.Json;

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
internal class lab3
{
    private static List<Book> books = new List<Book>();
    private const string FileName = "books.json";
    static void Main(string[] args)
    {
        LoadBooks();

        while (true)
        {
            Console.WriteLine("Menu:");
            Console.WriteLine("1. Add a new book");
            Console.WriteLine("2. View list of books");
            Console.WriteLine("3. Delete book by name");
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
                    RemoveBookByName(Name);
                    Console.WriteLine($"Book {Name} was deleted.");
                    break;

                default:
                    Console.WriteLine("Invalid choice");
                    break;
            }
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

        Console.Write("Date: ");
        var date = Console.ReadLine();

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
    private static void SaveBooks()
    {
        var json = JsonSerializer.Serialize(books, new JsonSerializerOptions { WriteIndented = true });
        File.WriteAllText(FileName, json);
    }
    private static void LoadBooks()
    {
        if (File.Exists(FileName))
        {
            try
            {
                var json = File.ReadAllText(FileName);
                books = JsonSerializer.Deserialize<List<Book>>(json);
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
    private static void RemoveBookByName(string name)
    {
        books.RemoveAll(s => s.Name.Equals(name, StringComparison.OrdinalIgnoreCase));
    }
}