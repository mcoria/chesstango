package chess.uci.arbiter;

import chess.board.representations.fen.FENDecoder;
import chess.uci.engine.imp.EngineProxy;
import chess.uci.engine.imp.EngineZonda;
import chess.uci.arbiter.imp.EngineControllerImp;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Mauricio Coria
 *
 */
public class MatchTest {

    private static EngineControllerImp engine1;
    private static EngineControllerImp engine2;


    @BeforeClass
    public static void setup() {
        engine1 = new EngineControllerImp(new EngineZonda());
        engine2 = new EngineControllerImp(new EngineProxy());

        engine1.send_CmdUci();
        engine1.send_CmdIsReady();

        engine2.send_CmdUci();
        engine2.send_CmdIsReady();
    }

    @AfterClass
    public static void tearDown() {
        engine1.send_CmdQuit();
        engine2.send_CmdQuit();
    }

    @Test
    public void test01() {
        Match match = new Match(engine1, engine2, 1);

        Match.MathResult result = null;
        result = match.compete(FENDecoder.INITIAL_FEN);

        Assert.assertEquals(2, result.getWhitePoints() + result.getBlackPoints());
    }

    @Test
    public void test02() {
        Match match = new Match(engine1, engine2, 1);

        List<Match.MathResult> results = null;

        results = match.play(FENDecoder.INITIAL_FEN);

        Assert.assertEquals(2, results.size()  );
        Assert.assertEquals(4, results.stream().mapToInt( result -> result.getWhitePoints() +  result.getBlackPoints() ).sum()  );
    }

    @Test
    public void test03() {
        Match match = new Match(engine1, engine2, 1);

        List<Match.MathResult> results = null;

        List<String> fenList = Arrays.asList(FENDecoder.INITIAL_FEN, "6r1/8/k7/8/3P4/P4K1R/8/3N4 b - - 0 1");

        results = match.play(fenList);

        Assert.assertEquals(4, results.size()  );
        Assert.assertEquals(8, results.stream().mapToInt( result -> result.getWhitePoints() +  result.getBlackPoints() ).sum()  );
    }
}
