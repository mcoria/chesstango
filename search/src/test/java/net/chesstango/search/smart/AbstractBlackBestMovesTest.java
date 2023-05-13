
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractBlackBestMovesTest {

    public abstract SearchMove getBestMoveFinder();

    @Test
    public void test_moveQueen() {
        // hay que sacar a la reina negra de donde esta, sino se la morfa el caballo
        Game game = FENDecoder.loadGame("r1b1kb1r/ppp1ppp1/n2q1n2/1N1P3p/3P4/5N2/PPP2PPP/R1BQKB1R b KQkq - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().search(game, 2);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.d6, smartMove.getFrom().getSquare());
        assertNotEquals(Square.a3, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.c5, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.c6, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.e6, smartMove.getTo().getSquare(), "Queen captured by pawn");
        assertNotEquals(Square.f4, smartMove.getTo().getSquare(), "Queen captured by bishop");
        assertNotEquals(Square.g3, smartMove.getTo().getSquare(), "Queen captured by pawn");
    }

    @Test
    public void test_imminentMateIn2Moves() {
        // Black will be in checkmate in the next 1 move
        Game game = FENDecoder.loadGame("8/2kQ2P1/8/1pP5/8/1B3P2/3R4/6K1 b - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().search(game, 2);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.c7, smartMove.getFrom().getSquare());
        assertEquals(Square.b8, smartMove.getTo().getSquare(), "There is no other option for black King");

        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }


    @Test
    public void test_imminentMateIn4Moves() {
        // Black will be in checkmate in the next 2 move
        Game game = FENDecoder.loadGame("8/2kQ4/6P1/1pP5/8/1B3P2/3R4/6K1 b - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().search(game, 4);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KING_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.c7, smartMove.getFrom().getSquare());
        assertEquals(Square.b8, smartMove.getTo().getSquare(), "There is no other option for black King");

        assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    //5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 2 55

    @Test
    public void test_Mate() {
        // Black can win the game in the next move
        Game game = FENDecoder.loadGame("5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().search(game, 5);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.b1, smartMove.getFrom().getSquare());

        Square to = smartMove.getTo().getSquare();
        assertTrue(Square.a1.equals(to) || Square.a2.equals(to) || Square.b3.equals(to) || Square.b4.equals(to) || Square.b5.equals(to));

        assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

    @Test //Max Walter vs. Emanuel Lasker
    public void test_MateInThree() {

        Game game = FENDecoder.loadGame("4r1k1/3n1ppp/4r3/3n3q/Q2P4/5P2/PP2BP1P/R1B1R1K1 b - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().search(game, 5);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.ROOK_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.e6, smartMove.getFrom().getSquare());
        assertEquals(Square.g6, smartMove.getTo().getSquare());

        assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

}
