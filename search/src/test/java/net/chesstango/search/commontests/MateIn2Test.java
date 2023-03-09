
package net.chesstango.search.commontests;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.search.SearchMove;
import net.chesstango.search.SearchMoveResult;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Mauricio Coria
 */
public abstract class MateIn2Test {

    public abstract SearchMove getBestMoveFinder();

    @Test
    public void testQueenMove1() {
        Game game = FENDecoder.loadGame("5K2/1b6/2p1Bp2/2N1k3/1B3p2/2pQ2n1/1b2pp2/8 w - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 3);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.d3, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.c4, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    @Test
    public void testQueenMove2() {
        Game game = FENDecoder.loadGame("8/8/5p2/1R1Nk3/R7/7K/8/1Q6 w - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 3);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.b1, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.f5, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    @Test
    public void testKnightMove1() {
        Game game = FENDecoder.loadGame("r2qkb1r/pp2nppp/3p4/2pNN1B1/2BnP3/3P4/PPP2PPP/R2bK2R w KQkq - 1 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 3);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.KNIGHT_WHITE, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.d5, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.f6, smartMove.getTo().getSquare());

        Assert.assertEquals(GameEvaluator.WHITE_WON, searchResult.getEvaluation());
    }

    //Robert Thacker vs. Bobby Fischer
    @Test
    public void testPromotion() {
        Game game = FENDecoder.loadGame("2r2r2/6kp/3p4/3P4/4Pp2/5P1P/PP1pq1P1/4R2K b - - 0 1");

        SearchMoveResult searchResult = getBestMoveFinder().searchBestMove(game, 3);

        Move smartMove = searchResult.getBestMove();

        Assert.assertEquals(Piece.PAWN_BLACK, smartMove.getFrom().getPiece());
        Assert.assertEquals(Square.d2, smartMove.getFrom().getSquare());
        Assert.assertEquals(Square.e1, smartMove.getTo().getSquare());

        Assert.assertTrue(smartMove instanceof MovePromotion);
        Assert.assertEquals(Piece.KNIGHT_BLACK, ((MovePromotion) smartMove).getPromotion());

        Assert.assertEquals(GameEvaluator.BLACK_WON, searchResult.getEvaluation());
    }

}
