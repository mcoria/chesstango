
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
public abstract class MateIn4Test {

    public abstract SearchMove getBestMoveFinder();

    @Test //Viktor Korchnoi vs. Peterson
    public void test1() {
        Game game = FENDecoder.loadGame("r2r1n2/pp2bk2/2p1p2p/3q4/3PN1QP/2P3R1/P4PP1/5RK1 w - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 7);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.g4, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.g7, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }


    @Test //Paul Keres vs. Arturo Pomar Salamanca
    public void test2() {
        Game game = FENDecoder.loadGame("7R/r1p1q1pp/3k4/1p1n1Q2/3N4/8/1PP2PPP/2B3K1 w - - 1 0");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 7);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.ROOK_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.h8, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.d8, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    @Test //Alexander Meek vs. Paul Morphy
    public void test3() {
        Game game = FENDecoder.loadGame("Q7/p1p1q1pk/3p2rp/4n3/3bP3/7b/PP3PPK/R1B2R2 b - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 7);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.BISHOP_BLACK, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.h3, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.g2, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

}
