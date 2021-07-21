import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class PokeAVG {
    public static void main (String[] args) throws InterruptedException {
        //specify a database to retrieve information from
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

}