package net.chesstango.uci.arena;

import net.chesstango.board.representations.fen.FEN;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.dummy.Dummy;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.arena.matchtypes.MatchByDepth;
import net.chesstango.uci.engine.UciTango;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class MatchTest {

    private EngineControllerImp smartEngine;

    private EngineControllerImp dummyEngine;

    @BeforeEach
    public void setup() {
        smartEngine = new EngineControllerImp(new UciTango())
                .overrideEngineName("Smart");

        dummyEngine = new EngineControllerImp(new UciTango(new Tango(new Dummy())))
                .overrideEngineName("Dummy");

        smartEngine.startEngine();
        dummyEngine.startEngine();
    }

    @AfterEach
    public void tearDown() {
        smartEngine.send_CmdQuit();
        dummyEngine.send_CmdQuit();
    }

    @Test
    public void testCompete() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(3));

        match.setFen(FEN.of(FENDecoder.INITIAL_FEN));

        match.compete();

        MatchResult result = match.createResult();

        // Deberia ganar el engine smartEngine
        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(smartEngine, result.getWinner());
    }

    @Test
    public void testPlay() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(3));
        //match.setDebugEnabled(true);

        MatchResult matchResult = match.play(FEN.of(FENDecoder.INITIAL_FEN));

        assertNotNull(matchResult);

        // Deberia ganar el engine smartEngine
        assertEquals(smartEngine, matchResult.getEngineWhite());
    }

    @Test
    public void testCreateResult01() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(1));

        match.setFen(FEN.of("8/P7/5Q1k/3p3p/3P2P1/1P1BP3/5P2/3K4 b - - 5 48"));
        match.setGame(FENDecoder.loadGame("8/P7/5Q1k/3p3p/3P2P1/1P1BP3/5P2/3K4 b - - 5 48"));

        MatchResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertEquals(smartEngine, result.getWinner());
    }

    @Test
    public void testCreateResult02() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(1));

        match.setFen(FEN.of("3k4/5p2/1p1bp3/3p2p1/3P3P/5q1K/p7/8 w - - 0 48"));
        match.setGame(FENDecoder.loadGame("3k4/5p2/1p1bp3/3p2p1/3P3P/5q1K/p7/8 w - - 0 48"));

        MatchResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertEquals(dummyEngine, result.getWinner());
    }


    @Test
    public void testCreateResultDraw01() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(1));

        match.setFen(FEN.of("6Q1/P7/7k/3p3p/3P3P/1P1BP3/5P2/3K4 b - - 5 48"));
        match.setGame(FENDecoder.loadGame("6Q1/P7/7k/3p3p/3P3P/1P1BP3/5P2/3K4 b - - 5 48"));

        MatchResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertNull(result.getWinner());
    }

    @Test
    public void testCreateResultDraw02() {
        Match match = new Match(smartEngine, dummyEngine, new MatchByDepth(1));

        match.setFen(FEN.of("3k4/5p2/1p1bp3/3p3p/3P3P/7K/p7/6q1 w - - 5 48"));
        match.setGame(FENDecoder.loadGame("3k4/5p2/1p1bp3/3p3p/3P3P/7K/p7/6q1 w - - 5 48"));

        MatchResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertNull(result.getWinner());
    }

}
