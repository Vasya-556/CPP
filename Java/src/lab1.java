import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
public class lab1 {
    public static void main(String[] args) {
        List<Toy> toys = readFile("toys.txt");

        toys.stream()
                .filter(toy -> toy.ageRange.equals("4-10 years"))
                .sorted(Comparator.comparing(toy -> toy.name))
                .forEach(toy -> {
                    System.out.println(toy.name + " - " + toy.price + " - " + toy.ageRange + " - " + toy.specialAttribute);
                });
    }
    public static List<Toy> readFile(String fileName) {
        List<Toy> toys = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(", ");
                toys.add(new Toy(parts[0], Integer.parseInt(parts[1]), parts[2], parts[3]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return toys;
    }
}

class Toy {
    String name;
    int price;
    String ageRange;
    String specialAttribute;

    public Toy(String name, int price, String ageRange, String specialAttribute) {
        this.name = name;
        this.price = price;
        this.ageRange = ageRange;
        this.specialAttribute = specialAttribute;
    }
}