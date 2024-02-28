package net.chesstango.search.smart;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.factory.SingletonMoveFactories;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactory;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.SearchMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * @author Mauricio Coria
 */
public abstract class GenericTest {

    private MoveFactory moveFactoryWhite = SingletonMoveFactories.getDefaultMoveFactoryWhite();

    protected SearchMove searchMove;

    @Test
    public void testHorizonteEffectCapture() {
        Game game = FENDecoder.loadGame("3q3k/3r4/8/3p4/8/8/3R4/3Q3K w - - 0 1");

        Move bestMove = searchMove.search(game).getBestMove();

        Move rookCapturePawn = moveFactoryWhite.createCaptureRookMove(PiecePositioned.getPiecePositioned(Square.d2, Piece.ROOK_WHITE), PiecePositioned.getPiecePositioned(Square.d5, Piece.PAWN_BLACK), Cardinal.Norte);

        assertNotEquals(rookCapturePawn, bestMove);
    }

    @Test
    public void testHorizonteEffectPromotion() {
        Game game = FENDecoder.loadGame("6k1/8/8/8/3Q4/2n5/3p3K/8 w - - 2 1");

        Move bestMove = searchMove.search(game).getBestMove();

        Move queenCaptureKnight = moveFactoryWhite.createCaptureQueenMove(PiecePositioned.getPiecePositioned(Square.d4, Piece.QUEEN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), Cardinal.SurOeste);

        assertNotEquals(queenCaptureKnight, bestMove);
    }

    @Test
    public void testDeterministicMove() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Move bestMove = searchMove.search(game).getBestMove();

        Game gameMirror = game.mirror();

        Move bestMoveMirror = searchMove.search(gameMirror).getBestMove();


        assertEquals(bestMove.getFrom().getPiece().getOpposite(), bestMoveMirror.getFrom().getPiece());
        assertEquals(bestMove.getFrom().getSquare().getMirrorSquare(), bestMoveMirror.getFrom().getSquare());

        assertEquals(bestMove.getTo().getSquare().getMirrorSquare(), bestMoveMirror.getTo().getSquare());
    }

}
