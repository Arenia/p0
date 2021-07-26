import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.List;
import com.opencsv.CSVReader;

//TODO: Construct with knowledge of maven, reliant on SQLite packages as well as OpenCSV

public class DBCrafter {
    public static void main(String[] args){
        /*
        if(args.length >= 1){
            try (CSVReader reader = new CSVReader(new FileReader("file.csv"))) {
                List<String[]> r = reader.readAll();
                r.forEach(x -> System.out.println(Arrays.toString(x)));
            }
            catch(FileNotFoundException ex){
                System.out.println("File does not exist, check your args and try again.");
            }
            catch(Exception ex){
                System.err.println("Error: " + ex.getMessage());
            }
        }
        else {
            System.out.println("Usage: java DBCrafter (file to read)");
        }
         */
        readCSV(args[0]);
    }

    public static void readCSV(String filename){
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> r = reader.readAll();
            r.forEach(x -> System.out.println(Arrays.toString(x)));
        }
        catch(FileNotFoundException ex){
            System.out.println("File does not exist, check your call and try again.");
        }
        catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
