import org.apache.commons.lang3.ArrayUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.Scanner;

public class Pokedex {
    private final String pathname;
    private Connection connection = null;
    private Statement retriever;
    private final String saveFile;
    private final boolean saving;
    private Pokemon results;

    private Pokedex(Path filename, boolean save, String saveFile){
        this.pathname = "jdbc:sqlite:"+filename;
        this.saving = save;
        this.saveFile = saveFile;
    }

    public static Pokedex of(Path filename, boolean save, String saveFile){
        return new Pokedex(filename, save, saveFile);
    }

    private Statement getConnection(){
        try {
            connection = DriverManager.getConnection(pathname);
            retriever = connection.createStatement();
            retriever.setQueryTimeout(30); //Sets timeout to 30 seconds
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        return retriever;
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

    public Pokemon searchPokemonID(int search_term) {
        retriever = getConnection();
        ResultSet mon;
        try {
            mon = retriever.executeQuery("select * from pokemon where id=" + search_term);
            results = Pokemon.resultMon(mon);
            if(saving){
                results.savePokemon(this.saveFile);
            }
            else{
                results.printPokemon();
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        return results;
    }

    public Pokemon searchPokemonName(String search_term) {
        retriever = getConnection();
        ResultSet mon;
        try {
            mon = retriever.executeQuery("select * from pokemon where name='" + search_term+"'");
            results = Pokemon.resultMon(mon);
            if(saving){
                results.savePokemon(this.saveFile);
            }
            else{
                results.printPokemon();
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        return results;
    }

    public String[] searchType(String search_term, int search_type) {
        String[] search_results;
        switch (search_type){
            case 1:
                search_results = searchAllType(search_term);
                break;
            case 2:
                search_results = searchEvolvedType(search_term);
                break;
            case 3:
                search_results = searchPlusType(search_term);
                break;
            case 4:
                search_results = searchLittleType(search_term);
                break;
            case 5:
                search_results = searchFlatType(search_term);
                break;
            case 6:
                search_results = searchMonoType(search_term);
                break;
            case 7:
                search_results = searchMonoEvolvedType(search_term);
                break;
            case 8:
                search_results = searchMonoPlusType(search_term);
                break;
            case 9:
                search_results = searchMonoLittleType(search_term);
                break;
            default:
                return null;
        }
        return search_results;
    }

    public String[] searchType(String search_term) {
        String[] search_results;
        Scanner read_in = new Scanner(System.in);
        System.out.println("Select a mode to work in:");
        System.out.println("1. All Pokemon");
        System.out.println("2. Evolved only");
        System.out.println("3. Evolved plus Non-Evolving Pokemon");
        System.out.println("4. Not Fully Evolved Only");
        System.out.println("5. Non-Evolving Pokemon Only");
        System.out.println("6. Monotype All Only");
        System.out.println("7. Monotype Evolved Only");
        System.out.println("8. Monotype Evolved Plus Non-Evolving");
        System.out.println("9. Monotype Not Fully Evolved");
        System.out.println("0. Cancel");
        System.out.print("Enter your selection: ");
        String input = read_in.nextLine();
        switch (input){
            case "1":
                search_results = searchAllType(search_term);
                break;
            case "2":
                search_results = searchEvolvedType(search_term);
                break;
            case "3":
                search_results = searchPlusType(search_term);
                break;
            case "4":
                search_results = searchLittleType(search_term);
                break;
            case "5":
                search_results = searchFlatType(search_term);
                break;
            case "6":
                search_results = searchMonoType(search_term);
                break;
            case "7":
                search_results = searchMonoEvolvedType(search_term);
                break;
            case "8":
                search_results = searchMonoPlusType(search_term);
                break;
            case "9":
                search_results = searchMonoLittleType(search_term);
                break;
            default:
                return null;
        }
        return search_results;
    }

    private String[] searchMonoLittleType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            mons = retriever.executeQuery("select * from pokemon where (evolved<2) AND (type1='"+search_term+"' AND type2='-')");
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchMonoPlusType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (evolved>1) AND (type1='"+search_term+"' AND type2='-')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchMonoEvolvedType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (evolved=2) AND (type1='"+search_term+"' AND type2='-')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchMonoType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (type1='"+search_term+"' AND type2='-')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchFlatType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (evolved=3) AND (type1='"+search_term+"' OR type2='"+search_term+"')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;


            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchLittleType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (evolved<2) AND (type1='"+search_term+"' OR type2='"+search_term+"')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;


            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchPlusType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where (evolved>1) AND (type1='"+search_term+"' OR type2='"+search_term+"')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }


    private String[] searchEvolvedType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where evolved=2 AND (type1='"+search_term+"' OR type2='"+search_term+"')");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private String[] searchAllType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
        String[] search_results = new String[9];
        sb.append("Total pokemon found: ");
        try{
            int hp=0;
            int attack=0;
            int defense=0;
            int sp_at=0;
            int sp_def=0;
            int spd=0;
            int count=0;
            mons = retriever.executeQuery("select * from pokemon where type1='"+search_term+"' OR type2='"+search_term+"'");
            while(mons.next()){
                sb.append(mons.getString("name")).append(", ");
                hp += mons.getInt("hp");
                attack += mons.getInt("attack");
                defense += mons.getInt("defense");
                sp_at += mons.getInt("sp_attack");
                sp_def += mons.getInt("sp_defense");
                spd += mons.getInt("speed");
                count += 1;
            }
            sb.delete((sb.length() - 2), sb.length());
            float avg_hp = (float) hp / count;
            float avg_attack = (float) attack / count;
            float avg_defense = (float) defense / count;
            float avg_sp_at = (float) sp_at / count;
            float avg_sp_def = (float) sp_def / count;
            float avg_spd = (float) spd / count;
            float avg_total = avg_hp + avg_attack + avg_defense + avg_sp_at + avg_sp_def + avg_spd;

            search_results[0] = "--Final results--";
            search_results[1] = sb.toString();
            search_results[2] = "Average HP: "+avg_hp;
            search_results[3] = "Average Attack: "+avg_attack;
            search_results[4] = "Average Defense: "+avg_defense;
            search_results[5] = "Average Special Attack: "+avg_sp_at;
            search_results[6] = "Average Special Defense: "+avg_sp_def;
            search_results[7] = "Average Speed: "+avg_spd;
            search_results[8] = "Average Base Total: "+avg_total;
            if(saving){
                saveSearchResults(search_results);
            }
            else{
                printSearchResults(search_results);
            }
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
        if(ArrayUtils.isEmpty(search_results)){
            return null;
        }
        else{
            return search_results;
        }
    }

    private void printSearchResults(String[] search_results){
        for (String line : search_results) {
            System.out.println(line);
        }
    }

    private void saveSearchResults(String[] search_results){
        Path save_path = Paths.get(this.saveFile);
        try (BufferedWriter writer = Files.newBufferedWriter(save_path)){
            for (String line : search_results) {
                writer.write(line+"\n");
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
