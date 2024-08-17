package ar.edu.itba.ss.g2;

import ar.edu.itba.ss.g2.model.Particle;
import ar.edu.itba.ss.g2.simulation.BoardState;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** Unit test for simple App. */
public class AppTest {
    private static final Set<Particle> PARTICLES =
            Set.of(
                    new Particle(0L, 6.967936329590714, 5.106726641618282, 0.0),
                    new Particle(1L, 6.252909929468449, 16.977231508896566, 0.0),
                    new Particle(2L, 9.961604691650708, 12.049154569511684, 0.0),
                    new Particle(3L, 9.793472231271972, 5.252840291706278, 0.0),
                    new Particle(4L, 14.854743059934814, 15.515876053513296, 0.0),
                    new Particle(5L, 12.269055993245628, 9.338848296117533, 0.0),
                    new Particle(6L, 12.848017374753873, 14.254183888708008, 0.0),
                    new Particle(7L, 2.1886493838926024, 16.391871447775188, 0.0),
                    new Particle(8L, 9.33477138904281, 4.872677181474343, 0.0),
                    new Particle(9L, 12.569028059591554, 2.818236900498119, 0.0),
                    new Particle(10L, 15.557375623809271, 0.31793739183198166, 0.0),
                    new Particle(11L, 12.528318352169322, 9.789297928077783, 0.0),
                    new Particle(12L, 2.400562459099176, 16.502148923626272, 0.0),
                    new Particle(13L, 7.793495864578683, 8.055936131662488, 0.0),
                    new Particle(14L, 1.4278766522132336, 2.947779667134791, 0.0),
                    new Particle(15L, 13.09722362958614, 8.615678470714839, 0.0),
                    new Particle(16L, 7.294496098248892, 3.0939210606388268, 0.0),
                    new Particle(17L, 12.750725369652585, 16.36387395050971, 0.0),
                    new Particle(18L, 16.677308091542006, 7.635707977733985, 0.0),
                    new Particle(19L, 18.89856486479054, 8.652778288924024, 0.0),
                    new Particle(20L, 19.03235328905104, 5.739506794871938, 0.0),
                    new Particle(21L, 5.557242040302679, 18.1914545917204, 0.0),
                    new Particle(22L, 0.7046947011842275, 15.948702907795552, 0.0),
                    new Particle(23L, 15.480335864796082, 18.44451398631993, 0.0),
                    new Particle(24L, 3.432963406794778, 12.855041511674823, 0.0),
                    new Particle(25L, 18.351300053424456, 9.777528024696995, 0.0),
                    new Particle(26L, 6.9724904892213075, 11.805047998537393, 0.0),
                    new Particle(27L, 6.608699833984495, 7.2901836265017845, 0.0),
                    new Particle(28L, 11.57919615001488, 8.492932998118102, 0.0),
                    new Particle(29L, 5.56396976477952, 14.7124455647651, 0.0),
                    new Particle(30L, 7.016970293903142, 3.873673256807346, 0.0),
                    new Particle(31L, 0.1218775175575515, 5.563164316770672, 0.0),
                    new Particle(32L, 15.856390252025152, 4.31718133932047, 0.0),
                    new Particle(33L, 5.787947928372779, 16.59729188991293, 0.0),
                    new Particle(34L, 6.29949754502634, 9.951502013983973, 0.0),
                    new Particle(35L, 9.144569073716903, 9.911695832059992, 0.0),
                    new Particle(36L, 0.14440600165127826, 11.382751454111444, 0.0),
                    new Particle(37L, 8.880385121543725, 3.7782969001363487, 0.0),
                    new Particle(38L, 17.1433702779106, 2.436374559635257, 0.0),
                    new Particle(39L, 3.5822543785764105, 11.082452827359454, 0.0),
                    new Particle(40L, 16.77614223260903, 8.867633028897963, 0.0),
                    new Particle(41L, 14.530459670977432, 18.367591146771247, 0.0),
                    new Particle(42L, 8.600203082514511, 17.983579508742743, 0.0),
                    new Particle(43L, 19.294394267757674, 6.378387768867335, 0.0),
                    new Particle(44L, 6.1350686441554325, 13.110972450513398, 0.0),
                    new Particle(45L, 17.46410365514699, 10.523809249995406, 0.0),
                    new Particle(46L, 16.120915433552803, 17.921074605183964, 0.0),
                    new Particle(47L, 4.205607096642819, 7.633639022208307, 0.0),
                    new Particle(48L, 13.999836738028186, 3.511400325830114, 0.0),
                    new Particle(49L, 13.654575431579852, 1.8739912139495551, 0.0),
                    new Particle(50L, 3.1218341495879542, 14.229120013807552, 0.0),
                    new Particle(51L, 14.367817738636843, 19.50927704075763, 0.0),
                    new Particle(52L, 16.856847330531753, 12.802316650230864, 0.0),
                    new Particle(53L, 17.267597397744577, 16.350207183818714, 0.0),
                    new Particle(54L, 2.7594774569696456, 9.116528620390572, 0.0),
                    new Particle(55L, 2.7198548756162277, 17.668122233491985, 0.0),
                    new Particle(56L, 15.30894094211725, 10.000034123610998, 0.0),
                    new Particle(57L, 9.848798059169685, 13.197794911032137, 0.0),
                    new Particle(58L, 1.2172624804256404, 9.061837516505637, 0.0),
                    new Particle(59L, 14.644012081607078, 1.4772046543080708, 0.0),
                    new Particle(60L, 15.44954328613693, 17.494471276990083, 0.0),
                    new Particle(61L, 9.934258796171358, 18.704614439540986, 0.0),
                    new Particle(62L, 2.9147422845740456, 5.110465715815424, 0.0),
                    new Particle(63L, 5.233058732815154, 18.6408386037183, 0.0),
                    new Particle(64L, 19.674157966723513, 17.405234729111566, 0.0),
                    new Particle(65L, 9.665701160757322, 12.661660126308323, 0.0),
                    new Particle(66L, 5.3319829510009225, 3.0820561261573998, 0.0),
                    new Particle(67L, 15.639052038758779, 7.32938011435845, 0.0),
                    new Particle(68L, 3.891645803050483, 5.989808235717547, 0.0),
                    new Particle(69L, 3.9246149800590757, 4.49897507425767, 0.0),
                    new Particle(70L, 11.347764851046245, 14.337527291008573, 0.0),
                    new Particle(71L, 7.440290306226986, 17.538070883635008, 0.0),
                    new Particle(72L, 2.023872473598418, 6.205102312442971, 0.0),
                    new Particle(73L, 1.6813572308448599, 11.528417900143213, 0.0),
                    new Particle(74L, 7.173335550479365, 11.968832444754904, 0.0),
                    new Particle(75L, 10.762251936771307, 14.499526990600984, 0.0),
                    new Particle(76L, 15.992120315079186, 10.882747556320973, 0.0),
                    new Particle(77L, 1.9181964574151977, 15.274993880185548, 0.0),
                    new Particle(78L, 18.719118294491867, 6.517063890182708, 0.0),
                    new Particle(79L, 7.097004110586306, 9.62138265526976, 0.0),
                    new Particle(80L, 19.56795784710627, 12.71530508629139, 0.0),
                    new Particle(81L, 15.378693053306263, 6.092177986369811, 0.0),
                    new Particle(82L, 9.412145602604877, 8.597493181591025, 0.0),
                    new Particle(83L, 12.653382066049836, 1.06274748287849, 0.0),
                    new Particle(84L, 11.881730836689218, 18.445329720529248, 0.0),
                    new Particle(85L, 9.552864110050795, 4.217469102676241, 0.0),
                    new Particle(86L, 3.4861760769391403, 2.639365672443932, 0.0),
                    new Particle(87L, 10.227551808556383, 1.8428929852509324, 0.0),
                    new Particle(88L, 14.984860957639587, 4.37849727872784, 0.0),
                    new Particle(89L, 11.623748561448949, 19.697655903934944, 0.0),
                    new Particle(90L, 13.833674445576552, 0.2363678386137602, 0.0),
                    new Particle(91L, 10.387873006744975, 7.103143916533052, 0.0),
                    new Particle(92L, 19.697426488021573, 13.609107209238982, 0.0),
                    new Particle(93L, 8.722927848739321, 7.111915387908548, 0.0),
                    new Particle(94L, 3.681820607227335, 6.847262959283538, 0.0),
                    new Particle(95L, 3.8612239969197892, 10.832078530900791, 0.0),
                    new Particle(96L, 6.64984277876057, 9.174261469887728, 0.0),
                    new Particle(97L, 6.000850165485916, 18.856329766736383, 0.0),
                    new Particle(98L, 0.8588479726778275, 16.366534257889757, 0.0),
                    new Particle(99L, 6.097676851926024, 16.432220556827826, 0.0));

    private static final Map<Long, List<Long>> MAP;

    private static final int L = 20;
    private static final double RC = 1.0;
    private static final int M = 19;

    static {
        Map<Long, List<Long>> map = new HashMap<>();

        map.put(1L, List.of(33L, 99L));
        map.put(2L, List.of(65L));
        map.put(3L, List.of(8L));
        map.put(5L, List.of(11L));
        map.put(7L, List.of(12L));
        map.put(8L, List.of(3L, 85L));
        map.put(11L, List.of(5L));
        map.put(12L, List.of(7L));
        map.put(16L, List.of(30L));
        map.put(20L, List.of(43L, 78L));
        map.put(21L, List.of(63L, 97L));
        map.put(22L, List.of(98L));
        map.put(23L, List.of(41L, 46L, 60L));
        map.put(26L, List.of(74L));
        map.put(30L, List.of(16L));
        map.put(32L, List.of(88L));
        map.put(33L, List.of(1L, 99L));
        map.put(34L, List.of(79L, 96L));
        map.put(37L, List.of(85L));
        map.put(39L, List.of(95L));
        map.put(41L, List.of(23L));
        map.put(43L, List.of(20L, 78L));
        map.put(46L, List.of(23L, 60L));
        map.put(47L, List.of(94L));
        map.put(51L, List.of(90L));
        map.put(57L, List.of(65L));
        map.put(60L, List.of(23L, 46L));
        map.put(63L, List.of(21L, 97L));
        map.put(65L, List.of(2L, 57L));
        map.put(68L, List.of(94L));
        map.put(70L, List.of(75L));
        map.put(74L, List.of(26L));
        map.put(75L, List.of(70L));
        map.put(78L, List.of(20L, 43L));
        map.put(79L, List.of(34L, 96L));
        map.put(80L, List.of(92L));
        map.put(85L, List.of(8L, 37L));
        map.put(88L, List.of(32L));
        map.put(90L, List.of(51L));
        map.put(92L, List.of(80L));
        map.put(94L, List.of(47L, 68L));
        map.put(95L, List.of(39L));
        map.put(96L, List.of(34L, 79L));
        map.put(97L, List.of(21L, 63L));
        map.put(98L, List.of(22L));
        map.put(99L, List.of(1L, 33L));

        MAP = Map.copyOf(map); // Ensure the map is immutable
    }

    /** Rigorous Test :-) */
    @Test
    @SuppressWarnings("unchecked")
    public void test()
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        BoardState boardState = new BoardState(L, 0, 0, RC, PARTICLES);

        // need to access getNeighbours method, but it is private
        // so we need to use reflection to access it
        Method method = BoardState.class.getDeclaredMethod("getNeighbours");
        method.setAccessible(true);

        Map<Particle, Set<Particle>> neighbours =
                (Map<Particle, Set<Particle>>) method.invoke(boardState);


        for (Map.Entry<Long, List<Long>> entry : MAP.entrySet()) {
            Particle p1 = PARTICLES.stream().filter(p -> p.getId() == entry.getKey()).findFirst().get();
            Set<Particle> expectedNeighbours = Set.of(entry.getValue().stream().map(id -> PARTICLES.stream().filter(p -> p.getId() == id).findFirst().get()).toArray(Particle[]::new));

            assert neighbours.get(p1).equals(expectedNeighbours);
        }
    }
}
