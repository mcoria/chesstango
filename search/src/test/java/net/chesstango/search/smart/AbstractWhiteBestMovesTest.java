
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractWhiteBestMovesTest {

    public abstract SearchMove getBestMoveFinder();

    @Test
    public void test_moveQueen() {
        // hay que sacar a la reina blanca de donde esta, sino se la morfa el caballo
        Game game = FENDecoder.loadGame("r1bqkb1r/ppp2ppp/5n2/3p4/1n1p3P/N2Q1N2/PPP1PPP1/R1B1KB1R w KQkq - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 2);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.d3, smartMove.getFrom().getSquare());
        Assert.assertNotEquals("Queen captured by pawn", Square.a6, smartMove.getTo().getSquare());
        Assert.assertNotEquals("Queen captured by pawn", Square.c4, smartMove.getTo().getSquare());
        Assert.assertNotEquals("Queen captured by pawn", Square.c3, smartMove.getTo().getSquare());
        Assert.assertNotEquals("Queen captured by pawn", Square.e3, smartMove.getTo().getSquare());
        Assert.assertNotEquals("Queen captured by bishop", Square.f5, smartMove.getTo().getSquare());
        Assert.assertNotEquals("Queen captured by pawn", Square.g6, smartMove.getTo().getSquare());
    }

    @Test
    public void test_imminentMateIn2Moves() {
        // White will be in checkmate in the next 1 move
        Game game = FENDecoder.loadGame("6k1/3r4/1b3p2/8/1Pp5/8/2Kq2p1/8 w - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 2);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.KING_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.c2, smartMove.getFrom().getSquare());
        Assert.assertEquals("There is no other option for black King", Square.b1, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }


    @Test
    public void test_imminentMateIn4Moves() {
        // White will be in checkmate in the next 2 move
        Game game = FENDecoder.loadGame("6k1/3r4/1b3p2/8/1Pp5/6p1/2Kq4/8 w - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 4);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.KING_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.c2, smartMove.getFrom().getSquare());
        Assert.assertEquals("There is no other option for black King", Square.b1, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

    //5R2/6p1/2p1pp2/3p4/K1k5/8/8/1q6 b - - 2 55

    @Test
    public void test_Mate() {
        // White can win the game in the next move
        Game game = FENDecoder.loadGame("1Q6/8/8/k1K5/3P4/2P1PP2/6P1/5r2 w - - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 5);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.b8, smartMove.getFrom().getSquare());

        Square to = smartMove.getTo().getSquare();
        Assert.assertTrue(Square.a8.equals(to) || Square.a7.equals(to) || Square.b6.equals(to) || Square.b5.equals(to) || Square.b4.equals(to));

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    @Test //Max Walter vs. Emanuel Lasker
    public void test_MateInThree() {

        Game game = FENDecoder.loadGame("r1b1r1k1/pp2bp1p/5p2/q2p4/3N3Q/4R3/3N1PPP/4R1K1 w - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 5);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.e3, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.g3, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

}
