import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;


public class Pokeavg {
    public static void main (String[] args) {
        //specify a database to retrieve information from
        try (CSVReader reader = new CSVReader(new FileReader(args[0]))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> {
                for (String s : x) {
                    System.out.println(s);
                }
            });
        }
        catch(FileNotFoundException ex){
            System.out.println("File does not exist, check your call and try again.");
        }
        catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
    }

}