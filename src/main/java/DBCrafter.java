import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//TODO: Clean up, perhaps create a data collection for pkmn data

public class DBCrafter {
    public static List<String[]> readCSV(String filename){
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> data = reader.readAll();
            //Throw out top row of CSV
            data.remove(0);
            return data;
        }
        catch(FileNotFoundException ex){
            System.err.println("File does not exist, check your file path and try again.");
            System.exit(1);
        }
        catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
            System.exit(1);
        }
        return null;
    }

    public static void saveDB(List<String[]> data){
        Connection connection = null;
        try{

            //create connection
            connection = DriverManager.getConnection("jdbc:sqlite:pokemon.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); //Sets timeout to 30 seconds

            statement.executeUpdate("drop table if exists pokemon");
            statement.executeUpdate("create table pokemon (id int, name string, type1 string, type2 string, hp int, attack int, defense int, sp_attack int, sp_defense int, speed int)");

            //Use StringBuilder to conform data to table insertion
            StringBuilder sb = new StringBuilder();
            sb.append("values(");
            data.forEach( x-> {
                //flush sb each pass
                sb.delete(0, sb.length());
                sb.append("values(");
                for( String s : x ){
                    if(StringUtils.isNumeric(s)){
                        sb.append(s).append(", ");
                    }
                    else{
                        //Capitalization check
                        if(s.length() > 0){
                            s = s.substring(0,1).toUpperCase() + s.substring(1);
                        }
                        sb.append("'").append(s).append("'").append(", ");
                    }
                }
                sb.replace((sb.length()-2), sb.length(), ")");
                try {
                    statement.executeUpdate("insert into pokemon "+ sb);
                } catch (SQLException ex) {
                    //System.err.println(ex.getMessage());
                    System.err.println("Bad syntax");
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
                System.out.println("Connection close failed.");
                System.err.println(ex.getMessage());
            }
        }
    }

    public static List<String[]> readDB(){
        List<String[]> data = new ArrayList<>();
        //create connection
        Connection connection;
        ResultSet rs;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:pokemon.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); //Sets timeout to 30 seconds

            rs = statement.executeQuery("select * from pokemon");
            ArrayList<String> line = new ArrayList<>();
            while(rs.next()){
                //id, name, type1, type2, hp, attack, defense, sp_attack, sp_defense, speed
                line.add(String.valueOf(rs.getInt("id")));
                line.add(rs.getString("name"));
                line.add(rs.getString("type1"));
                line.add(rs.getString("type2"));
                line.add(String.valueOf(rs.getInt("hp")));
                line.add(String.valueOf(rs.getInt("attack")));
                line.add(String.valueOf(rs.getInt("defense")));
                line.add(String.valueOf(rs.getInt("sp_attack")));
                line.add(String.valueOf(rs.getInt("sp_defense")));
                line.add(String.valueOf(rs.getInt("speed")));
                data.add((String[]) line.toArray());
                line.clear();
            }
            return data;
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }
}
