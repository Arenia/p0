import com.opencsv.CSVReader;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DBCrafter {
    private final String pathname;
    private Connection connection = null;
    private Statement updater;

    private DBCrafter(Path filename){
        this.pathname = "jdbc:sqlite:"+filename;
    }

    public static DBCrafter of(Path filename){
        return new DBCrafter(filename);
    }

    public Path getPath() {
        return Paths.get(pathname.substring(12));
    }

    private Statement getConnection(){
        try {
            connection = DriverManager.getConnection(pathname);
            updater = connection.createStatement();
            updater.setQueryTimeout(30); //Sets timeout to 30 seconds
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return updater;
    }

    public void readCSV(String filename){
        try (CSVReader reader = new CSVReader(new FileReader(filename))) {
            List<String[]> data = reader.readAll();
            //Throw out top row of CSV
            data.remove(0);
            saveDB(data);
        }
        catch(FileNotFoundException ex){
            System.err.println("File does not exist, check your file path and try again.");
            System.exit(1);
        }
        catch(Exception ex){
            System.err.println("Error: " + ex.getMessage());
            System.exit(1);
        }
    }

    private Statement batchData(List<String[]> data){
        Statement batch = getConnection();
        //Use StringBuilder to conform data to table insertion
        StringBuilder sb = new StringBuilder();
        //Iterate for each item in data, add to batch update
        for (String[] x : data) {
            //flush sb each pass
            sb.delete(0, sb.length());
            sb.append("values(");
            for (String s : x) {
                if (StringUtils.isNumeric(s)) {
                    sb.append(s).append(", ");
                } else {
                    //Capitalization check
                    if (s.length() > 0) {
                        s = s.substring(0, 1).toUpperCase() + s.substring(1);
                    }
                    sb.append("'").append(s).append("'").append(", ");
                }
            }
            sb.replace((sb.length() - 2), sb.length(), ")");
            try {
                batch.addBatch("insert into pokemon " + sb);
            } catch (SQLException ex) {
                //System.err.println(ex.getMessage());
                System.err.println("Bad syntax");
            }
        }
        return batch;
    }

    private void makeFile(){
        //Flush and recreate the table in case of existing
        updater = getConnection();
        try{
            updater.addBatch("drop table if exists pokemon");
            updater.addBatch("create table pokemon (id int, name string, type1 string, type2 string, hp int, attack int, defense int, sp_attack int, sp_defense int, speed int, evolved int)");
            //Execute batch, empty after for safety
            updater.executeBatch();
            updater.clearBatch();
        }
        catch(SQLException ex)
        {
            System.err.println(ex.getMessage());
        }

    }

    private void saveDB(List<String[]> data){
        try{
            System.out.println("Starting DB save, this may take a minute!");

            makeFile();

            updater = batchData(data);

            //Execute batch, empty after for safety
            updater.executeBatch();
            updater.clearBatch();
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        finally
        {
            closeConnection();
        }
        System.out.println("Database save complete");
    }

    private void closeConnection() {
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
