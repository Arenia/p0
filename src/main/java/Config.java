import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class Config {
    private final String dbFile;
    private final String csvFile;
    private final String saveFile;
    private boolean reload = false;
    private boolean save = false;
    private final String search;
    private final int search_type;
    private final boolean isType;

    public String getSaveFile() {
        return saveFile;
    }

    public boolean isReload() {
        return reload;
    }

    public boolean isSave() {
        return save;
    }

    public boolean isType(){
        return isType;
    }

    public String getSearch() {
        return search;
    }

    public int getSearch_type() {
        return search_type;
    }

    public Path getDBFile() {
        return Paths.get(dbFile);
    }

    public String getCSVFile() {
        return csvFile;
    }

    public Config(String[] args) {
        Properties props = new Properties();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--csv":
                case "-c":
                    //Specify csv to read from
                    props.setProperty("csv", args[++i]);
                    break;
                case "--database":
                case "-d":
                    //Specify database to save to
                    props.setProperty("db", args[++i]);
                    break;
                case "--search":
                case "-s":
                    //Specify what to search on
                    props.setProperty("search", args[++i]);
                    break;
                case "--search-type":
                case "-t":
                    //Specify what search category to use
                    props.setProperty("search_type", args[++i]);
                    break;
                case "force-reload":
                case "-F":
                    //Force a reload of the database
                    this.reload = true;
                    break;
                case "--output":
                case "-o":
                    //Save search to a file
                    this.save = true;
                    props.setProperty("save-to", args[++i]);
                    break;
                default:
                    System.err.println("Unknown argument: "+args[i]);
                    System.exit(1);
            }
        }

        this.dbFile = props.getProperty("db", "pokemon.db");
        this.csvFile = props.getProperty("csv", "pokemon.csv");
        this.saveFile = props.getProperty("save-to", "");
        this.search = props.getProperty("search", "").toLowerCase();
        this.search_type = Integer.parseInt(props.getProperty("search_type", "0"));

        switch(this.search){
            case "normal:":
            case "fire":
            case "water":
            case "grass":
            case "electric":
            case "ice":
            case "fighting":
            case "poison":
            case "ground":
            case "flying":
            case "psychic":
            case "bug":
            case "rock":
            case "ghost":
            case "dark":
            case "dragon":
            case "steel":
            case "fairy":
                isType = true;
                break;
            default:
                isType = false;
                break;
        }
    }
}
