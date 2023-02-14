package net.chesstango.uci.arena;

import net.chesstango.search.dummy.Dummy;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.uci.arena.imp.EngineControllerImp;
import net.chesstango.uci.engine.imp.EngineTango;
import org.junit.*;

import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class MatchTest {

    private static EngineControllerImp smartEngine;
    private static EngineControllerImp dummyEngine;


    @BeforeClass
    public static void setup() {
        smartEngine = new EngineControllerImp(new EngineTango());
        dummyEngine = new EngineControllerImp(new EngineTango(new Dummy()));

        smartEngine.send_CmdUci();
        smartEngine.send_CmdIsReady();

        dummyEngine.send_CmdUci();
        dummyEngine.send_CmdIsReady();
    }

    @AfterClass
    public static void tearDown() {
        smartEngine.send_CmdQuit();
        dummyEngine.send_CmdQuit();
    }

    @Test
    public void test01() {
        Match match = new Match(smartEngine, dummyEngine, 1);

        MathResult result = match.compete(FENDecoder.INITIAL_FEN);

        // Deberia ganar el engine  blanco dado que el engine en negro ejecuta la funcion basica de evaluacion
        Assert.assertTrue(result.getPoints() > 0 );
    }

    @Test
    public void test02() {
        Match match = new Match(smartEngine, dummyEngine, 1);

        List<MathResult> matchResult = match.play(FENDecoder.INITIAL_FEN);

        Assert.assertEquals(2, matchResult.size()  );

        // Deberia ganar el engine  blanco dado que el engine en negro ejecuta la funcion basica de evaluacion
        Assert.assertEquals(GameEvaluator.WHITE_WON, matchResult.stream().filter(result -> result.getEngineWhite() == smartEngine).mapToLong(MathResult::getPoints).sum() );
        Assert.assertEquals(GameEvaluator.BLACK_WON, matchResult.stream().filter(result -> result.getEngineBlack() == smartEngine).mapToLong(MathResult::getPoints).sum() );
    }

}
