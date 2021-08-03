import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.Scanner;

public class Pokeavg {
    public static void main (String ...args) {
        Logger log = LoggerFactory.getLogger(Pokeavg.class);
        log.info("Starting up...");

        final Context appContext = new Context(args);
        Config appConfig = appContext.getAppConfig();

        //Check if db exists, if not populate
        log.info("Checking for database...");
        Pokedex pokedex = appContext.getPokedex();
        pathDBCheck(appContext.getDatabase(), appConfig);

        log.info("Starting search function.");
        startSearch(pokedex, appConfig);
    }

    private static void startSearch(Pokedex pokedex, Config appConfig) {
        String search = appConfig.getSearch();
        if(search.isEmpty()){
            startGUISearch(pokedex);
        }
        else{
            if(appConfig.isType()){
                searchType(pokedex, appConfig);
            }
            else{
                if(StringUtils.isNumeric(search)){
                    pokedex.searchPokemonID(Integer.parseInt(search));
                }
                else{
                    //Confirm name is capitalized
                    search = search.substring(0, 1).toUpperCase() + search.substring(1);
                    pokedex.searchPokemonName(search);
                }
            }
        }
    }

    private static void startGUISearch(Pokedex pokedex){
        Scanner read_in = new Scanner(System.in);
        boolean loop = true;
        while(loop){
            System.out.print("--Search Menu--\nPick a type (T)\nPick a pokemon (P)\nQuit (Q)\nEnter your selection: ");
            String search_term = read_in.nextLine().toUpperCase();
            switch (search_term) {
                case "T":
                    searchGUIType(pokedex);
                    break;
                case "P":
                    getGUISingleData(pokedex);
                    break;
                case "Q":
                    loop = false;
                    break;
                default:
                    System.out.println("Unrecognized command. Exiting.");
                    loop = false;
                    break;
            }
        }
    }

    private static void searchType(Pokedex pokedex, Config appConfig) {
        String search = appConfig.getSearch();
        search = search.substring(0, 1).toUpperCase() + search.substring(1);
        if(appConfig.getSearch_type() == 0){
            pokedex.searchType(search);
        }
        else{
            pokedex.searchType(search, appConfig.getSearch_type());
        }
    }

    private static void searchGUIType(Pokedex pokedex) {
        Scanner read_in = new Scanner(System.in);
        System.out.println("Give me a pokemon type to look through: ");
        String search_term = read_in.nextLine();
        search_term = search_term.substring(0, 1).toUpperCase() + search_term.substring(1);
        pokedex.searchType(search_term);
    }

    private static void pathDBCheck(DBCrafter pkdb, Config appConfig){
        if (new File(String.valueOf(pkdb.getPath())).isFile()) {
            if(appConfig.isReload()){
                pkdb.readCSV(appConfig.getCSVFile());
            }
        }
        else {
            pkdb.readCSV(appConfig.getCSVFile());
        }
    }

    private static void getGUISingleData(Pokedex pokedex) {
        Scanner read_in = new Scanner(System.in);
        System.out.println("Give me a pokemon name or dex number to look up: ");
        String search_term = read_in.nextLine();
        if(StringUtils.isNumeric(search_term)) {
            pokedex.searchPokemonID(Integer.parseInt(search_term));
        }
        else{
            //Confirm name is capitalized
            search_term = search_term.substring(0, 1).toUpperCase() + search_term.substring(1);
            pokedex.searchPokemonName(search_term);
        }
    }
}