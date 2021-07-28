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
            getSingleData(args[1]);
        } else {
            Scanner read_in = new Scanner(System.in);
            System.out.print("Specify a pokemon id to look up: ");
            getSingleData(read_in.nextLine());
        }
    }

    private static void DBCheck(String[] args){
        List<String[]> data;
        if (new File("pokemon.db").isFile()) {
            Scanner read_in = new Scanner(System.in);
            System.out.print("Repopulate DB? (Y/N)");
            String result = read_in.nextLine().toUpperCase();
            if(result.equals("Y")){
                if (args.length > 0) {
                    data = DBCrafter.readCSV(args[0]);
                } else {
                    read_in = new Scanner(System.in);
                    System.out.print("Specify a csv to populate the db with: ");
                    data = DBCrafter.readCSV(read_in.nextLine());
                }
                DBCrafter.saveDB(data);
            }
        }
        else {

            if (args.length > 0) {
                data = DBCrafter.readCSV(args[0]);
            } else {
                Scanner read_in = new Scanner(System.in);
                System.out.print("Specify a csv to populate the db with: ");
                data = DBCrafter.readCSV(read_in.nextLine());
            }
            DBCrafter.saveDB(data);
        }
    }

    private static void getSingleData(String name) {
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