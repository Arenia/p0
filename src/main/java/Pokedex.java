import java.nio.file.Path;
import java.sql.*;
import java.util.Scanner;

public class Pokedex {
    private final String pathname;
    private Connection connection = null;
    private Statement retriever;

    private Pokedex(Path filename){
        this.pathname = "jdbc:sqlite:"+filename;
    }

    public static Pokedex of(Path filename){
        return new Pokedex(filename);
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

    public void searchPokemon(int search_term) {
        retriever = getConnection();
        ResultSet mon;
        try {
            mon = retriever.executeQuery("select * from pokemon where id=" + search_term);
            System.out.println("Name: "+mon.getString(1));
            System.out.println("Pokedex number: "+mon.getInt(0));
            System.out.println("Type 1: "+mon.getString(2));
            System.out.println("Type 2: "+mon.getString(3));
            System.out.println("Base HP: "+mon.getInt(4));
            System.out.println("Base Attack: "+mon.getInt(5));
            System.out.println("Base Defense: "+mon.getInt(6));
            System.out.println("Base Special Attack: "+mon.getInt(7));
            System.out.println("Base Special Defense: "+mon.getInt(8));
            System.out.println("Base Speed: "+mon.getInt(9));
            int sum = mon.getInt(9) + mon.getInt(8) + mon.getInt(7) + mon.getInt(6) + mon.getInt(5) + mon.getInt(4);
            System.out.println("Base Total: "+sum);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    public void searchPokemon(String search_term) {
        retriever = getConnection();
        ResultSet mon;
        try {
            mon = retriever.executeQuery("select * from pokemon where name='" + search_term+"'");
            System.out.println("Name: "+mon.getString(1));
            System.out.println("Pokedex number: "+mon.getInt(0));
            System.out.println("Type 1: "+mon.getString(2));
            System.out.println("Type 2: "+mon.getString(3));
            System.out.println("Base HP: "+mon.getInt(4));
            System.out.println("Base Attack: "+mon.getInt(5));
            System.out.println("Base Defense: "+mon.getInt(6));
            System.out.println("Base Special Attack: "+mon.getInt(7));
            System.out.println("Base Special Defense: "+mon.getInt(8));
            System.out.println("Base Speed: "+mon.getInt(9));
            int sum = mon.getInt(9) + mon.getInt(8) + mon.getInt(7) + mon.getInt(6) + mon.getInt(5) + mon.getInt(4);
            System.out.println("Base Total: "+sum);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    public void searchType(String search_term) {
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
                searchAllType(search_term);
                break;
            case "2":
                searchEvolvedType(search_term);
                break;
            case "3":
                searchPlusType(search_term);
                break;
            case "4":
                searchLittleType(search_term);
                break;
            case "5":
                searchFlatType(search_term);
                break;
            case "6":
                searchMonoType(search_term);
                break;
            case "7":
                searchMonoEvolvedType(search_term);
                break;
            case "8":
                searchMonoPlusType(search_term);
                break;
            case "9":
                searchMonoLittleType(search_term);
                break;
            default:
                break;
        }
    }

    private void searchMonoLittleType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchMonoPlusType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchMonoEvolvedType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchMonoType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchFlatType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchLittleType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchPlusType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }


    private void searchEvolvedType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }

    private void searchAllType(String search_term) {
        retriever = getConnection();
        ResultSet mons;
        StringBuilder sb = new StringBuilder();
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

            System.out.println("--Final results--");
            System.out.println(sb);
            System.out.println("Average HP: "+avg_hp);
            System.out.println("Average Attack: "+avg_attack);
            System.out.println("Average Defense: "+avg_defense);
            System.out.println("Average Special Attack: "+avg_sp_at);
            System.out.println("Average Special Defense: "+avg_sp_def);
            System.out.println("Average Speed: "+avg_spd);
            System.out.println("Average Base Total: "+avg_total);
        }
        catch(SQLException ex){
            System.err.println(ex.getMessage());
        }
        closeConnection();
    }
}
