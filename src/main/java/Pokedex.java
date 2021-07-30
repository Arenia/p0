import org.apache.commons.lang3.StringUtils;

import java.nio.file.Path;
import java.sql.*;

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
        retriever = getConnection();
        ResultSet mons;
        int hp=0, attack=0, defense=0, sp_at=0, sp_def=0, spd=0, count=0;
        float avg_hp=0, avg_attack=0, avg_defense=0, avg_sp_at=0, avg_sp_def=0, avg_spd=0, avg_total=0;
        StringBuilder sb = new StringBuilder();
        sb.append("Total pokemon found: ");
        try{
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
            avg_hp = (float)hp / count;
            avg_attack = (float)attack/count;
            avg_defense = (float)defense/count;
            avg_sp_at = (float)sp_at/count;
            avg_sp_def = (float)sp_def/count;
            avg_spd = (float)spd/count;
            avg_total = avg_hp+avg_attack+avg_defense+avg_sp_at+avg_sp_def+avg_spd;

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
