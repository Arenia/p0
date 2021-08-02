import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.apache.commons.lang3.StringUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.List;

public class DBCrafter {
    private final String pathname;
    private Connection connection = null;
    private Statement maker;
    private PreparedStatement insert_batch;

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
            maker = connection.createStatement();
            maker.setQueryTimeout(30); //Sets timeout to 30 seconds
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return maker;
    }

    private PreparedStatement getBatchInsert(){
        try{
            connection = DriverManager.getConnection(pathname);
            //11 fields
            insert_batch = connection.prepareStatement("insert into pokemon values(? , ? , ? ,  ? , ? , ? , ? , ? , ? , ? , ? )");
            insert_batch.setQueryTimeout(30);
        }
        catch(SQLException ex){
            System.out.println("Catch in creating the batch insert.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return insert_batch;
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
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
//        catch(Exception ex){
//            System.out.println("Catch in reading CSV.");
//            System.out.println("Error: " + ex.getMessage());
//            System.exit(1);
//        }
    }
    private PreparedStatement batchThirty(List<String[]> data, int batch_num){
        PreparedStatement batch = getBatchInsert();
        int limiter = Math.min((batch_num * 30) + 30, data.size());
        //Iterate for each item in data, add to batch update
        try {
            for (int j = (batch_num*30); j < limiter; j++) {
                String[] x = data.get(j);
                for (int i = 0, xLength = (x.length); i < xLength; i++) {
                    String s = x[i];
                    if (StringUtils.isNumeric(s)) {
                        batch.setInt(i + 1, Integer.parseInt(s));
                    } else {
                        //Capitalization check
                        if (s.length() > 0) {
                            s = s.substring(0, 1).toUpperCase() + s.substring(1);
                        }
                        batch.setString(i + 1, s);
                    }
                }
                batch.addBatch();
                batch.clearParameters();
            }
        }
        catch(SQLException ex){
            System.out.println("Catch in batching data.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return batch;
    }

    private PreparedStatement batchData(List<String[]> data){
        PreparedStatement batch = getBatchInsert();

        //Iterate for each item in data, add to batch update
        try {
            for (String[] x : data) {
                for (int i = 0, xLength = (x.length); i < xLength; i++) {
                    String s = x[i];
                    if (StringUtils.isNumeric(s)) {
                        batch.setInt(i+1, Integer.parseInt(s));
                    } else {
                        //Capitalization check
                        if (s.length() > 0) {
                            s = s.substring(0, 1).toUpperCase() + s.substring(1);
                        }
                        batch.setString(i+1, s);
                    }
                }
                batch.addBatch();
                batch.clearParameters();
            }
        }
        catch(SQLException ex){
            System.out.println("Catch in batching data.");
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return batch;
    }

    private void makeFile(){
        //Flush and recreate the table in case of existing
        maker = getConnection();
        try{
            maker.addBatch("drop table if exists pokemon");
            maker.addBatch("create table pokemon (id int, name string, type1 string, type2 string, hp int, attack int, defense int, sp_attack int, sp_defense int, speed int, evolved int)");
            //Execute batch, empty after for safety
            maker.executeBatch();
            maker.clearBatch();
        }
        catch(SQLException ex)
        {
            System.out.println("Catch in creating the file.");
            System.err.println(ex.getMessage());
        }

    }

    private void saveDB(List<String[]> data){
        try{
            System.out.println("Starting DB save...");
            System.out.println("Creating DB file...");
            makeFile();

            System.out.println("Preparing table data...");
            long start = System.currentTimeMillis();
            int batch_count = (data.size()/30)+1;
            for(int i=0; i<batch_count;i++){
                insert_batch = batchThirty(data, i);
                System.out.println("Sending batch "+i+"...");
                long startInternal = System.currentTimeMillis();
                insert_batch.executeBatch();
                insert_batch.clearBatch();
                System.out.println("batch time taken = " + (System.currentTimeMillis() - startInternal) + " ms");
            }
            long end = System.currentTimeMillis();
            System.out.println("total time taken = " + (end - start) + " ms");
            System.out.println("avg total time taken = " + (end - start)/ data.size() + " ms");
            //insert_batch = batchData(data);

            System.out.println("Inserting data, please wait...");
            //Execute batch, empty after for safety
            //insert_batch.executeBatch();
            //insert_batch.clearBatch();
        }
        catch(SQLException ex){
            System.out.println("Catch in saving, bad statement?");
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
