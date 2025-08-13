package net.chesstango.board.representations.pgn;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.gardel.epd.EPD;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.gardel.pgn.PGN;
import net.chesstango.gardel.pgn.PGNStringEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

import static net.chesstango.board.Square.a2;
import static net.chesstango.board.Square.a4;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class GameToPGNTest {

    private GameToPGN gameToPGN;

    @BeforeEach
    public void setup() {
        gameToPGN = new GameToPGN();
    }

    @Test
    public void test_encodeGame1() {
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.d7, Square.d5)
                .executeMove(Square.g1, Square.f3)
                .executeMove(Square.d5, Square.e4);

        PGN pgn = gameToPGN.decode(game);

        List<String> moves = pgn.getMoveList();
        assertEquals("e4", moves.getFirst());
        assertEquals("d5", moves.get(1));
        assertEquals("Nf3", moves.get(2));
        assertEquals("dxe4", moves.get(3));

        overrideHeaders(pgn);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[Result \"*\"]\n" +
                "\n" +
                "1. e4 d5 2. Nf3 dxe4 *";
        String encodedGame = new PGNStringEncoder().encode(pgn);
        assertEquals(expectedResult, encodedGame);
    }


    @Test
    public void testToEpd() {
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));
        game.executeMove(a2, a4);

        PGN pgn = game.encode();

        List<EPD> pgnToEpd = pgn.toEPD().toList();

        assertEquals(1, pgnToEpd.size());
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - sm a4; c5 \"result='*'\"; c6 \"clock=1\"; c7 \"totalClock=1\"; id \"f1c2a586\";", pgnToEpd.getFirst().toString());
    }


    @Test
    public void test_check_mate() {
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));

        game.executeMove(Square.e2, Square.e4)
                .executeMove(Square.e7, Square.e5)
                .executeMove(Square.f1, Square.c4)
                .executeMove(Square.b8, Square.c6)
                .executeMove(Square.d1, Square.f3)
                .executeMove(Square.f8, Square.c5)
                .executeMove(Square.f3, Square.f7);

        PGN pgn = gameToPGN.decode(game);
        overrideHeaders(pgn);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[Result \"1-0\"]\n" +
                "\n" +
                "1. e4 e5 2. Bc4 Nc6 3. Qf3 Bc5 4. Qxf7# 1-0";


        String encodedGame = new PGNStringEncoder().encode(pgn);

        assertEquals(expectedResult, encodedGame);
    }

    @Test
    public void test_draw() {
        Game game = Game.from(FEN.of("k7/7Q/K7/8/8/8/8/8 w - - 0 1"));
        game.executeMove(Square.h7, Square.c7);

        PGN pgn = gameToPGN.decode(game);
        overrideHeaders(pgn);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[FEN \"k7/7Q/K7/8/8/8/8/8 w - - 0 1\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n" +
                "1. Qc7 1/2-1/2";


        String encodedGame = new PGNStringEncoder().encode(pgn);

        assertEquals(expectedResult, encodedGame);
    }

    @Test
    public void test_check_draw() {
        Game game = Game.from(FEN.of("k7/8/K7/2Q5/8/8/8/8 w - - 1 1"));

        game.executeMove(Square.c5, Square.c6);
        game.executeMove(Square.a8, Square.b8);
        game.executeMove(Square.c6, Square.d6);
        game.executeMove(Square.b8, Square.a8);
        game.executeMove(Square.d6, Square.c7);

        PGN pgn = gameToPGN.decode(game);
        overrideHeaders(pgn);

        String expectedResult = "[Event \"Computer chess game\"]\n" +
                "[Site \"KANO-COMPUTER\"]\n" +
                "[Date \"2022.06.17\"]\n" +
                "[Round \"?\"]\n" +
                "[White \"mauricio\"]\n" +
                "[Black \"opponent\"]\n" +
                "[FEN \"k7/8/K7/2Q5/8/8/8/8 w - - 1 1\"]\n" +
                "[Result \"1/2-1/2\"]\n" +
                "\n" +
                "1. Qc6+ Kb8 2. Qd6+ Ka8 3. Qc7 1/2-1/2";


        String encodedGame = new PGNStringEncoder().encode(pgn);

        assertEquals(expectedResult, encodedGame);
    }


    public void overrideHeaders(PGN pgn) {
        pgn.setEvent("Computer chess game");
        pgn.setSite("KANO-COMPUTER");
        pgn.setDate("2022.06.17");
        pgn.setRound("?");
        pgn.setWhite("mauricio");
        pgn.setBlack("opponent");
    }

}
