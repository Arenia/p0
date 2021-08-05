import org.junit.Assert;
import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Before;

public class TestPokedex{
    Pokedex testDex;

    @Before
    public void setTestDex(){
        testDex = Pokedex.of(Paths.get("pokemon.db"), true, "test.txt");
    }

    @Test
    public void testSearchPokemonID(){
        Pokemon actual = testDex.searchPokemonID(411);
        String expected = "Bastiodon";
        Assert.assertTrue(expected.equals(actual.getName()));
    }

    @Test
    public void testSearchPokemonName(){
        Pokemon actual = testDex.searchPokemonName("Shieldon");
        int expected = 410;
        Assert.assertTrue(expected == actual.getID());
    }

    @Test
    public void testSearchMonoLittleType(){
        String[] actual = testDex.searchType("Grass", 9);
        String expected = "Average Base Total: 320.64";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchMonoPlusType(){
        String[] actual = testDex.searchType("Grass", 8);
        String expected = "Average Base Total: 494.82355";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchMonoEvolvedType(){
        String[] actual = testDex.searchType("Grass", 7);
        String expected = "Average Base Total: 499.8";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchMonoType(){
        String[] actual = testDex.searchType("Grass", 6);
        String expected = "Average Base Total: 391.14285";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchFlatType(){
        String[] actual = testDex.searchType("Grass", 5);
        String expected = "Average Base Total: 537.4545";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchLittleType(){
        String[] actual = testDex.searchType("Grass", 4);
        String expected = "Average Base Total: 323.0741";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchPlusType(){
        String[] actual = testDex.searchType("Grass", 3);
        String expected = "Average Base Total: 499.3962";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchEvolvedType(){
        String[] actual = testDex.searchType("Grass", 2);
        String expected = "Average Base Total: 489.4286";
        Assert.assertTrue(expected.equals(actual[8]));
    }

    @Test
    public void testSearchAllType(){
        String[] actual = testDex.searchType("Grass", 1);
        String expected = "Average Base Total: 410.41122";
        Assert.assertTrue(expected.equals(actual[8]));
    }
}