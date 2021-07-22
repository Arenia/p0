import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Pokeavg {
    public static void main (String[] args) throws InterruptedException {
        //specify a database to retrieve information from
        if( args.length >= 2 ) {
            File data = new File(args[0]);

            //retrieve data from specified DB
            Map<Integer, String> mons = new HashMap<>();
            Integer count_id = 1;
            try(Scanner s = new Scanner(data)){
                while(s.hasNext()){
                    String name = s.next();
                    System.out.println("Checking line " + count_id.toString());
                    mons.put(count_id, name);
                    count_id+=1;
                }
            }
            catch (FileNotFoundException ex) {
                System.out.println("Database file not found. Check path and retry.");
            }

            //Return name from ID given
            System.out.println(mons.get(Integer.valueOf(args[1])));
        }
        else {
            System.out.println("Usage: specify the path of the data file then the ID you wish to give, for example:\n java PokeAVG og151.txt 1");
        }
    }

}