package net.chesstango.uci.arena;

import net.chesstango.board.Piece;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.engine.Tango;
import net.chesstango.search.dummy.Dummy;
import net.chesstango.uci.engine.UciTango;
import net.chesstango.uci.arena.gui.EngineControllerImp;
import net.chesstango.uci.protocol.requests.CmdGo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public class MatchTest {

    private EngineControllerImp smartEngine;
    private EngineControllerImp dummyEngine;


    @BeforeEach
    public void setup() {
        smartEngine = new EngineControllerImp(new UciTango().setLogging(false)).overrideEngineName("Smart");
        dummyEngine = new EngineControllerImp(new UciTango(new Tango(new Dummy())).setLogging(false)).overrideEngineName("Dummy");

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
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(2));

        match.setFen(FENDecoder.INITIAL_FEN);
        match.setChairs(smartEngine, dummyEngine);

        match.compete();

        GameResult result = match.createResult();

        // Deberia ganar el engine smartEngine
        assertTrue(result.getPoints() > Match.WINNER_POINTS);
        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(smartEngine, result.getWinner());
    }

    @Test
    public void testPlay() {
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(2));
        //match.setDebugEnabled(true);

        List<GameResult> matchResult = match.play(FENDecoder.INITIAL_FEN);

        assertEquals(2, matchResult.size());

        // Deberia ganar el engine smartEngine
        assertEquals(1, matchResult.stream().filter(result -> result.getEngineWhite() == smartEngine && result.getWinner() == smartEngine).count());
        assertEquals(1, matchResult.stream().filter(result -> result.getEngineBlack() == smartEngine && result.getWinner() == smartEngine).count());
    }

    @Test
    public void testCreateResult01() {
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        match.setFen("8/P7/5Q1k/3p3p/3P2P1/1P1BP3/5P2/3K4 b - - 5 48");
        match.setChairs(smartEngine, dummyEngine);
        match.setGame(FENDecoder.loadGame("8/P7/5Q1k/3p3p/3P2P1/1P1BP3/5P2/3K4 b - - 5 48"));

        GameResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertEquals(smartEngine, result.getWinner());
        assertEquals(Match.WINNER_POINTS +
                6 * Match.getPieceValue(Piece.PAWN_WHITE) +
                2 * Math.abs(Match.getPieceValue(Piece.PAWN_BLACK)) +
                1 * Match.getPieceValue(Piece.QUEEN_WHITE) +
                1 * Match.getPieceValue(Piece.BISHOP_WHITE) +
                1 * Match.getPieceValue(Piece.KING_WHITE) +
                1 * Math.abs(Match.getPieceValue(Piece.KING_BLACK)
                ), result.getPoints());
    }

    @Test
    public void testCreateResult02() {
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        match.setFen("3k4/5p2/1p1bp3/3p2p1/3P3P/5q1K/p7/8 w - - 0 48");
        match.setChairs(smartEngine, dummyEngine);
        match.setGame(FENDecoder.loadGame("3k4/5p2/1p1bp3/3p2p1/3P3P/5q1K/p7/8 w - - 0 48"));

        GameResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertEquals(dummyEngine, result.getWinner());
        assertEquals(-1 * (Match.WINNER_POINTS +
                6 * Match.getPieceValue(Piece.PAWN_WHITE) +
                2 * Math.abs(Match.getPieceValue(Piece.PAWN_BLACK)) +
                1 * Match.getPieceValue(Piece.QUEEN_WHITE) +
                1 * Match.getPieceValue(Piece.BISHOP_WHITE) +
                1 * Match.getPieceValue(Piece.KING_WHITE) +
                1 * Math.abs(Match.getPieceValue(Piece.KING_BLACK))
        ), result.getPoints());
    }


    @Test
    public void testCreateResultDraw01() {
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        match.setFen("6Q1/P7/7k/3p3p/3P3P/1P1BP3/5P2/3K4 b - - 5 48");
        match.setChairs(smartEngine, dummyEngine);
        match.setGame(FENDecoder.loadGame("6Q1/P7/7k/3p3p/3P3P/1P1BP3/5P2/3K4 b - - 5 48"));

        GameResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertNull(result.getWinner());
        assertEquals(
                6 * Match.getPieceValue(Piece.PAWN_WHITE) +
                        2 * Match.getPieceValue(Piece.PAWN_BLACK) +
                        1 * Match.getPieceValue(Piece.QUEEN_WHITE) +
                        1 * Match.getPieceValue(Piece.BISHOP_WHITE) +
                        1 * Match.getPieceValue(Piece.KING_WHITE) +
                        1 * Match.getPieceValue(Piece.KING_BLACK)
                , result.getPoints());
    }

    @Test
    public void testCreateResultDraw02() {
        Match match = new Match(smartEngine, dummyEngine, new CmdGo().setGoType(CmdGo.GoType.DEPTH).setDepth(1));

        match.setFen("3k4/5p2/1p1bp3/3p3p/3P3P/7K/p7/6q1 w - - 5 48");
        match.setChairs(smartEngine, dummyEngine);
        match.setGame(FENDecoder.loadGame("3k4/5p2/1p1bp3/3p3p/3P3P/7K/p7/6q1 w - - 5 48"));

        GameResult result = match.createResult();

        assertEquals(smartEngine, result.getEngineWhite());
        assertEquals(dummyEngine, result.getEngineBlack());
        assertNull(result.getWinner());
        assertEquals(
                6 * Match.getPieceValue(Piece.PAWN_BLACK) +
                        2 * Match.getPieceValue(Piece.PAWN_WHITE) +
                        1 * Match.getPieceValue(Piece.QUEEN_BLACK) +
                        1 * Match.getPieceValue(Piece.BISHOP_BLACK) +
                        1 * Match.getPieceValue(Piece.KING_BLACK) +
                        1 * Match.getPieceValue(Piece.KING_WHITE)
                , result.getPoints());
    }

    @Test
    public void testPieceValues() {
        assertTrue(Match.getPieceValue(Piece.PAWN_WHITE) - Match.getPieceValue(Piece.PAWN_BLACK) == 2 * Match.getPieceValue(Piece.PAWN_WHITE));
        assertTrue(Match.getPieceValue(Piece.ROOK_WHITE) - Match.getPieceValue(Piece.ROOK_BLACK) == 2 * Match.getPieceValue(Piece.ROOK_WHITE));
        assertTrue(Match.getPieceValue(Piece.KNIGHT_WHITE) - Match.getPieceValue(Piece.KNIGHT_BLACK) == 2 * Match.getPieceValue(Piece.KNIGHT_WHITE));
        assertTrue(Match.getPieceValue(Piece.BISHOP_WHITE) - Match.getPieceValue(Piece.BISHOP_BLACK) == 2 * Match.getPieceValue(Piece.BISHOP_WHITE));
        assertTrue(Match.getPieceValue(Piece.QUEEN_WHITE) - Match.getPieceValue(Piece.QUEEN_BLACK) == 2 * Match.getPieceValue(Piece.QUEEN_WHITE));
        assertTrue(Match.getPieceValue(Piece.KING_WHITE) - Match.getPieceValue(Piece.KING_BLACK) == 2 * Match.getPieceValue(Piece.KING_WHITE));
    }
}
