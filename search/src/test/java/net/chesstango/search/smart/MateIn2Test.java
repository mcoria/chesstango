
package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;
import net.chesstango.gardel.fen.FENParser;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.search.Search;
import net.chesstango.search.SearchResult;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public abstract class MateIn2Test {

    protected Search search;

    @Test
    public void testQueenMove1() {
        Game game = Game.fromFEN("5K2/1b6/2p1Bp2/2N1k3/1B3p2/2pQ2n1/1b2pp2/8 w - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.d3, smartMove.getFrom().getSquare());
        assertEquals(Square.c4, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test
    public void testQueenMove2() {
        Game game = Game.fromFEN("8/8/5p2/1R1Nk3/R7/7K/8/1Q6 w - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.QUEEN_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.b1, smartMove.getFrom().getSquare());
        assertEquals(Square.f5, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    @Test
    public void testKnightMove1() {
        Game game = Game.fromFEN("r2qkb1r/pp2nppp/3p4/2pNN1B1/2BnP3/3P4/PPP2PPP/R2bK2R w KQkq - 1 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.KNIGHT_WHITE, smartMove.getFrom().getPiece());
        assertEquals(Square.d5, smartMove.getFrom().getSquare());
        assertEquals(Square.f6, smartMove.getTo().getSquare());

        assertEquals(Evaluator.WHITE_WON, searchResult.getBestEvaluation());
    }

    //Robert Thacker vs. Bobby Fischer
    @Test
    public void testPromotion() {
        Game game = Game.fromFEN("2r2r2/6kp/3p4/3P4/4Pp2/5P1P/PP1pq1P1/4R2K b - - 0 1");

        SearchResult searchResult = search.search(game);

        Move smartMove = searchResult.getBestMove();

        assertEquals(Piece.PAWN_BLACK, smartMove.getFrom().getPiece());
        assertEquals(Square.d2, smartMove.getFrom().getSquare());
        assertEquals(Square.e1, smartMove.getTo().getSquare());

        assertTrue(smartMove instanceof MovePromotion);
        assertEquals(Piece.KNIGHT_BLACK, ((MovePromotion) smartMove).getPromotion());

        assertEquals(Evaluator.BLACK_WON, searchResult.getBestEvaluation());
    }

}
