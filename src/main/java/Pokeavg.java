import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;


// Fill this class with methods to work off the db

public class Pokeavg {
    static Pokedex pokedex;

    public static void main (String[] args) {
        Logger log = LoggerFactory.getLogger(Pokeavg.class);
        log.info("Starting up...");

        //Check if db exists, if not populate
        log.info("Checking for database...");
        pokedex = Pokedex.of(DBCheck());

        log.info("Starting search function.");
        startSearch();
    }

    private static void startSearch(){
        Scanner read_in = new Scanner(System.in);
        boolean loop = true;
        while(loop){
            System.out.print("--Search Menu--\nPick a type (T)\nPick a pokemon (P)\nQuit (Q)\nEnter your selection: ");
            String search_term = read_in.nextLine().toUpperCase();
            switch (search_term) {
                case "T":
                    searchType();
                    break;
                case "P":
                    getSingleData();
                    break;
                default:
                    loop = false;
                    break;
            }
        }
    }

    private static void searchType() {
        Scanner read_in = new Scanner(System.in);
        System.out.println("Give me a pokemon type to look through: ");
        String search_term = read_in.nextLine();
        search_term = search_term.substring(0, 1).toUpperCase() + search_term.substring(1);
        pokedex.searchType(search_term);
    }

    private static Path DBCheck(){
        DBCrafter pkdb = DBCrafter.of(Paths.get("pokemon.db"));
        if (new File("pokemon.db").isFile()) {
            Scanner read_in = new Scanner(System.in);
            System.out.print("Repopulate DB? (Y/N) ");
            String result = read_in.nextLine().toUpperCase();
            if(result.equals("Y")){
                read_in = new Scanner(System.in);
                System.out.print("Specify a csv to populate the db with: ");
                pkdb.readCSV(read_in.nextLine());
            }
        }
        else {
            Scanner read_in = new Scanner(System.in);
            System.out.print("Specify a csv to populate the db with: ");
            pkdb.readCSV(read_in.nextLine());
        }
        return pkdb.getPath();
    }

    private static void getSingleData() {
        Scanner read_in = new Scanner(System.in);
        System.out.println("Give me a pokemon name or dex number to look up: ");
        String search_term = read_in.nextLine();
        if(!StringUtils.isNumeric(search_term)) {
            //Confirm name is capitalized
            search_term = search_term.substring(0, 1).toUpperCase() + search_term.substring(1);
        }
        if(StringUtils.isNumeric(search_term)) {
            pokedex.searchPokemon(Integer.parseInt(search_term));
        }
        else{
            pokedex.searchPokemon(search_term);
        }

    }
}