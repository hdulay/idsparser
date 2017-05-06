import info.debatty.java.stringsimilarity.NormalizedLevenshtein;

/**
 * Created by hkdulay on 5/6/17.
 */
public class Test {

    @org.junit.Test
    public void test() {
        NormalizedLevenshtein l = new NormalizedLevenshtein();

        System.out.println(l.distance("My string", "My $tring"));
        System.out.println(l.distance("My string", "My string"));
        System.out.println(l.distance("My string", "My $tring"));
    }
}
