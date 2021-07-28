import java.util.List;

// Fill this class with methods to work off the db

public class Pokeavg {
    public static void main (String[] args) {
        List<String[]> data = DBCrafter.readCSV(args[0]);
        if (data != null) {
            data.forEach(x -> {
                for (String s : x) {
                    System.out.print(s + ", ");
                }
                System.out.println();
            });
        }
        else{
            System.out.println("Read error?");
            System.exit(1);
        }
        //DBCrafter.saveDB(data);
    }
}