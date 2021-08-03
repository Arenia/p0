public class Context {
    private final Pokedex pokedex;
    private final Config appConfig;
    private final DBCrafter database;

    public Pokedex getPokedex() {
        return pokedex;
    }

    public Config getAppConfig() {
        return appConfig;
    }

    public DBCrafter getDatabase() {
        return database;
    }

    public Context(String ...args) {
        this.appConfig = new Config(args);
        this.database = DBCrafter.of(appConfig.getDBFile());
        boolean save = appConfig.isSave();
        String saveFile = appConfig.getSaveFile();
        this.pokedex = Pokedex.of(appConfig.getDBFile(), save, saveFile);
    }
}
