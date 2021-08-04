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
        String expected = "Shieldon";
        Assert.assertTrue(expected.equals(actual.getName()));
    }

    @Test
    public void testSearchMonoLittleType(){
        String[] actual = testDex.searchType("Grass", 9);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchMonoPlusType(){
        String[] actual = testDex.searchType("Grass", 8);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchMonoEvolvedType(){
        String[] actual = testDex.searchType("Grass", 7);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchMonoType(){
        String[] actual = testDex.searchType("Grass", 6);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchFlatType(){
        String[] actual = testDex.searchType("Grass", 5);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchLittleType(){
        String[] actual = testDex.searchType("Grass", 4);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchPlusType(){
        String[] actual = testDex.searchType("Grass", 3);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchEvolvedType(){
        String[] actual = testDex.searchType("Grass", 2);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }

    @Test
    public void testSearchAllType(){
        String[] actual = testDex.searchType("Grass", 1);
        String expected = "--Final results--";
        Assert.assertTrue(expected.equals(actual[0]));
    }
}