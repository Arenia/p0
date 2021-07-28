import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

// Fill this class with methods to work off the db

public class Pokeavg {
    public static void main (String[] args) {
        //Check if db exists, if not populate
        DBCheck(args);
        if (args.length > 1) {
            getData(args[1]);
        } else {
            Scanner sysin = new Scanner(System.in);
            System.out.print("Specify a pokemon id to look up: ");
            getData(sysin.nextLine());
        }
    }

    private static void DBCheck(String[] args){

        List<String[]> data;
        if (! new File("pokemon.db").isFile()) {
            if (args.length > 0) {
                data = DBCrafter.readCSV(args[0]);
            } else {
                Scanner sysin = new Scanner(System.in);
                System.out.print("Specify a csv to populate the db with: ");
                data = DBCrafter.readCSV(sysin.nextLine());
            }
            DBCrafter.saveDB(data);
        }
    }

    private static void getData(String name) {
        //Confirm name is capitalized
        name = name.substring(0,1).toUpperCase() + name.substring(1);
        Connection connection;
        ResultSet rs;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:pokemon.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30); //Sets timeout to 30 seconds

            rs = statement.executeQuery("select * from pokemon where name='" + name+"'");
            System.out.println("Pokedex ID number: " + rs.getInt("id"));
            System.out.println("Pokemon name: " + rs.getString("name"));
            System.out.println("Type 1: " + rs.getString("type1"));
            System.out.println("Type 2: " + rs.getString("type2"));
            System.out.println("HP: " + rs.getInt("hp"));
            System.out.println("Attack: " + rs.getInt("attack"));
            System.out.println("Defense: " + rs.getInt("defense"));
            System.out.println("Special Attack: " + rs.getInt("sp_attack"));
            System.out.println("Special Defense: " + rs.getInt("sp_defense"));
            System.out.println("Speed: " + rs.getInt("speed"));
            int bst = rs.getInt("hp") + rs.getInt("attack") + rs.getInt("defense")  + rs.getInt("sp_attack") + rs.getInt("sp_defense") + rs.getInt("speed");
            System.out.println("Base stat total: " + bst);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
    }
}