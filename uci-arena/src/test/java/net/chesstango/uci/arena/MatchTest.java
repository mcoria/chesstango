package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.dummy.Dummy;
import net.chesstango.uci.engine.EngineTango;
import net.chesstango.uci.gui.EngineControllerImp;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class MatchTest {

    private EngineControllerImp smartEngine;
    private EngineControllerImp dummyEngine;


    @Before
    public void setup() {
        smartEngine = new EngineControllerImp(new EngineTango()).overrideEngineName("Smart");
        dummyEngine = new EngineControllerImp(new EngineTango(new Dummy())).overrideEngineName("Dummy");

        smartEngine.send_CmdUci();
        smartEngine.send_CmdIsReady();

        dummyEngine.send_CmdUci();
        dummyEngine.send_CmdIsReady();
    }

    @After
    public void tearDown() {
        smartEngine.send_CmdQuit();
        dummyEngine.send_CmdQuit();
    }

    @Test
    public void testCompete() {
        Match match = new Match(smartEngine, dummyEngine, 1);

        match.setFen(FENDecoder.INITIAL_FEN);
        match.setChairs(smartEngine, dummyEngine);

        MathResult result = match.compete();

        // Deberia ganar el engine smartEngine
        Assert.assertEquals(GameEvaluator.WHITE_WON, result.getPoints());
        Assert.assertEquals(smartEngine, result.getEngineWhite());
    }

    @Test
    public void testPlay() {
        Match match = new Match(smartEngine, dummyEngine, 1);

        List<MathResult> matchResult = match.play(FENDecoder.INITIAL_FEN);

        Assert.assertEquals(2, matchResult.size());

        // Deberia ganar el engine smartEngine
        Assert.assertEquals(GameEvaluator.WHITE_WON, matchResult.stream().filter(result -> result.getEngineWhite() == smartEngine).mapToLong(MathResult::getPoints).sum());
        Assert.assertEquals(GameEvaluator.BLACK_WON, matchResult.stream().filter(result -> result.getEngineBlack() == smartEngine).mapToLong(MathResult::getPoints).sum());
    }

}
