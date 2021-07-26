import com.opencsv.CSVReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

//TODO: Construct with knowledge of maven, reliant on SQLite packages as well as OpenCSV

public class DBCrafter {
    public static void main(String[] args){
        List<String[]> data = readCSV(args[0]);
        if (data != null) {
            saveDB(data);
        }
        else{
            System.out.println("Bad read?");
        }
    }

    public static List<String[]> readCSV(String filename){
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            return reader.readAll();
        }
        catch(FileNotFoundException ex){
            System.out.println("File does not exist, check your call and try again.");
        }
        catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
        }
        return null;
    }

    private static void saveDB(List<String[]> data){
        Connection connection = null;
        try{

            //create connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); //Sets timeout to 30 seconds

            statement.executeUpdate("drop table if exists pokemon");
            statement.executeUpdate("create table pokemon (int id, string name, string type1, string type2, int hp, int attack, int defense, int sp_attack, int sp_defense, int speed)");

            //Use StringBuilder to conform data to table insertion
            StringBuilder sb = new StringBuilder();
            sb.append("values(");
            data.forEach( x-> {
                System.out.println(Arrays.toString(x));
                //flush sb each pass
                sb.delete(0, sb.length()-1);
                sb.append("values(");
                for( String s : x ){
                    sb.append(s).append(", ");
                }
                sb.replace((sb.length()-3), sb.length()-1, ")");
                try {
                    (statement.executeUpdate("insert into pokemon "+ sb));
                } catch (SQLException ex) {
                    System.err.println(ex.getMessage());
                }

            });


        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        finally
        {
            try
            {
                if(connection != null)
                    connection.close();
            }
            catch(SQLException ex)
            {
                // connection close failed.
                System.err.println(ex.getMessage());
            }
        }
    }
}
