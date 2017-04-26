package techpark.game;

import techpark.game.avatar.Square;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Варя on 18.04.2017.
 */
@SuppressWarnings({"StaticVariableNamingConvention",
        "AnonymousInnerClassMayBeStatic",
        "PublicField",
        "NonConstantFieldWithUpperCaseName",
        "InnerClassTooDeeplyNested",
        "MagicNumber"})
public class Config {
    public static int numberOfPlayers = 2;

    public static int WIDTH = 10;
    public static int HEIGHT = 10;

    public static List<Square> CONTROLPOINTS= Arrays.asList(new Square(0, 0),
            new Square(2, 2), new Square(5,5), new Square(9,9));

    public static int NUMBERENEMY = 10;
    public static double ENEMYSPEED = 0.5;
    public static double ENEMYHP = 4;
    public static double WAVECOFF = 2.5;


    public static HashMap<Character,List<Character>> COMBGEM =
            new HashMap<Character,List<Character>>(){{put('k', Arrays.asList('a', 'b', 'c'));
                put('l', Arrays.asList('c', 'd', 'e'));
                put('m', Arrays.asList('e', 'f', 'a'));
                put('z', Arrays.asList('k', 'l', 'm'));}};

    public static HashMap<Character, HashMap<String, Double>> TOWERS =
            new HashMap<Character,HashMap<String, Double>>(){{
                put('a', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.5);
                    put("DAMAGE", 1.0);
                    put("RADIUS", 2.0);
                }});
                put('b', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.5);
                    put("DAMAGE", 1.0);
                    put("RADIUS", 2.0);
                }});
                put('c', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.5);
                    put("DAMAGE", 1.0);
                    put("RADIUS", 2.0);
                }});
                put('d', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.5);
                    put("DAMAGE", 1.0);
                    put("RADIUS", 2.0);
                }});
                put('e', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.5);
                    put("DAMAGE", 1.0);
                    put("RADIUS", 2.0);
                }});
                put('k', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.25);
                    put("DAMAGE", 2.0);
                    put("RADIUS", 2.0);
                }});
                put('l', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.25);
                    put("DAMAGE", 2.0);
                    put("RADIUS", 2.0);
                }});
                put('m', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.25);
                    put("DAMAGE", 2.0);
                    put("RADIUS", 2.0);
                }});
                put('z', new HashMap<String, Double>(){{
                    put("FREQUENSY", 0.15);
                    put("DAMAGE", 3.0);
                    put("RADIUS", 3.0);
                }});
    }};

    public static Double MAXRADIUS = 3.0;
    public static Double THRONECOFF = 0.2;
}
