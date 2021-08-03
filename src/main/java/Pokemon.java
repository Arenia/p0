import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Pokemon {
    private final int id;
    private final String name;
    private final String type1;
    private final String type2;
    private final int hp;
    private final int attack;
    private final int defense;
    private final int sp_atk;
    private final int sp_def;
    private final int speed;
    private final int evolved;

    private Pokemon(String[] params){
        this.id = Integer.parseInt(params[0]);
        this.name = params[1];
        this.type1 = params[2];
        this.type2 = params[3];
        this.hp = Integer.parseInt(params[4]);
        this.attack = Integer.parseInt(params[5]);
        this.defense = Integer.parseInt(params[6]);
        this.sp_atk = Integer.parseInt(params[7]);
        this.sp_def = Integer.parseInt(params[8]);
        this.speed = Integer.parseInt(params[9]);
        this.evolved = Integer.parseInt(params[10]);
    }

    public static Pokemon resultMon(ResultSet mon) throws SQLException {
        String[] params = new String[11];
        params[0] = String.valueOf(mon.getInt(1));
        params[1] = mon.getString(2);
        params[2] = mon.getString(3);
        params[3] = mon.getString(4);
        params[4] = String.valueOf(mon.getInt(5));
        params[5] = String.valueOf(mon.getInt(6));
        params[6] = String.valueOf(mon.getInt(7));
        params[7] = String.valueOf(mon.getInt(8));
        params[8] = String.valueOf(mon.getInt(9));
        params[9] = String.valueOf(mon.getInt(10));
        params[10] = String.valueOf(mon.getInt(11));
        return new Pokemon(params);
    }

    public void printPokemon(){
        System.out.println("Name: "+this.name);
        System.out.println("Pokedex number: "+this.id);
        System.out.println("Type 1: "+this.type1);
        System.out.println("Type 2: "+this.type2);
        System.out.println("Base HP: "+this.hp);
        System.out.println("Base Attack: "+this.attack);
        System.out.println("Base Defense: "+this.defense);
        System.out.println("Base Special Attack: "+this.sp_atk);
        System.out.println("Base Special Defense: "+this.sp_def);
        System.out.println("Base Speed: "+this.speed);
        int sum = this.hp + this.attack + this.defense + this.sp_atk + this.sp_def +this.speed;
        System.out.println("Base Total: "+sum);
        switch (this.evolved){
            case 0:
                System.out.println("Evolution: Not yet evolved");
                break;
            case 1:
                System.out.println("Evolution: Partially evolved, not final");
                break;
            case 2:
                System.out.println("Evolution: Fully evolved");
                break;
            case 3:
                System.out.println("Evolution: Doesn't evolve");
                break;
            default:
                break;
        }
    }

    public void savePokemon(String saveFile) {
        Path save_path = Paths.get(saveFile);
        try (BufferedWriter writer = Files.newBufferedWriter(save_path)){
            writer.write("Name: "+this.name+"\n");
            writer.write("Pokedex number: "+this.id+"\n");
            writer.write("Type 1: "+this.type1+"\n");
            writer.write("Type 2: "+this.type2+"\n");
            writer.write("Base HP: "+this.hp+"\n");
            writer.write("Base Attack: "+this.attack+"\n");
            writer.write("Base Defense: "+this.defense+"\n");
            writer.write("Base Special Attack: "+this.sp_atk+"\n");
            writer.write("Base Special Defense: "+this.sp_def+"\n");
            writer.write("Base Speed: "+this.speed+"\n");
            int sum = this.hp + this.attack + this.defense + this.sp_atk + this.sp_def +this.speed;
            writer.write("Base Total: "+sum+"\n");
            switch (this.evolved){
                case 0:
                    writer.write("Evolution: Not yet evolved"+"\n");
                    break;
                case 1:
                    writer.write("Evolution: Partially evolved, not final"+"\n");
                    break;
                case 2:
                    writer.write("Evolution: Fully evolved"+"\n");
                    break;
                case 3:
                    writer.write("Evolution: Doesn't evolve"+"\n");
                    break;
                default:
                    break;
            }
            writer.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }


}
