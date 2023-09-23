using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CS
{
    public class lab1
    {
        static void Main(string[] args)
        {
            List<Toy> toys = ReadFile("toys.txt");

            foreach (var toy in toys.Where(t => t.AgeRange == "4-10 years").OrderBy(t => t.Name))
            {
                Console.WriteLine($"{toy.Name} - {toy.Price} - {toy.AgeRange} - {toy.SpecialAttribute}");
            }

            Console.ReadKey();

        }
        public static List<Toy> ReadFile(string fileName)
        {
            var toys = new List<Toy>();

            try
            {
                using (StreamReader sr = new StreamReader(fileName))
                {
                    string line;
                    while ((line = sr.ReadLine()) != null)
                    {
                        string[] parts = line.Split(new string[] { ", " }, StringSplitOptions.None);
                        toys.Add(new Toy(parts[0], int.Parse(parts[1]), parts[2], parts[3]));
                    }
                }
            }
            catch (IOException e)
            {
                Console.WriteLine(e.Message);
            }

            return toys;
        }
    }
    public class Toy
    {
        public string Name { get; set; }
        public int Price { get; set; }
        public string AgeRange { get; set; }
        public string SpecialAttribute { get; set; }

        public Toy(string name, int price, string ageRange, string specialAttribute)
        {
            Name = name;
            Price = price;
            AgeRange = ageRange;
            SpecialAttribute = specialAttribute;
        }
    }
}
