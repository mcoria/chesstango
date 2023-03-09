package net.chesstango.search.smartminmax;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.MirrorBuilder;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.factories.MoveFactoryWhite;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.DefaultGameEvaluator;
import net.chesstango.evaluation.imp.GameEvaluatorByMaterial;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MinMaxPruning01Test {

    private MoveFactoryWhite moveFactoryWhite = new MoveFactoryWhite();

    private MinMaxPruning minMaxPruning = null;

    @Before
    public void setUp() {
        minMaxPruning = new MinMaxPruning();
        minMaxPruning.setGameEvaluator(new GameEvaluatorByMaterial());
    }

    @Test
    public void testHorizonteEffectCapture() {
        Game game = FENDecoder.loadGame("3q3k/3r4/8/3p4/8/8/3R4/3Q3K w - - 0 1");

        Move bestMove = minMaxPruning.searchBestMove(game, 1).getBestMove();

        Move rookCapturePawn = moveFactoryWhite.createCaptureRookMove(PiecePositioned.getPiecePositioned(Square.d2, Piece.ROOK_WHITE), PiecePositioned.getPiecePositioned(Square.d5, Piece.PAWN_BLACK), Cardinal.Norte);

        Assert.assertNotEquals(rookCapturePawn, bestMove);
    }

    @Test
    public void testHorizonteEffectPromotion() {
        Game game = FENDecoder.loadGame("6k1/8/8/8/3Q4/2n5/3p3K/8 w - - 2 1");

        Move bestMove = minMaxPruning.searchBestMove(game, 1).getBestMove();

        Move queenCaptureKnight = moveFactoryWhite.createCaptureMove(PiecePositioned.getPiecePositioned(Square.d4, Piece.QUEEN_WHITE), PiecePositioned.getPiecePositioned(Square.c3, Piece.KNIGHT_BLACK), Cardinal.SurOeste);

        Assert.assertNotEquals(queenCaptureKnight, bestMove);
    }

    @Test
    public void testDeterministicMove() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        Move bestMove = minMaxPruning.searchBestMove(game, 1).getBestMove();

        MirrorBuilder<Game> mirror = new MirrorBuilder(new GameBuilder());
        game.getChessPosition().constructBoardRepresentation(mirror);
        Game gameMirror = mirror.getChessRepresentation();

        Move bestMoveMirror = minMaxPruning.searchBestMove(gameMirror, 1).getBestMove();


        Assert.assertEquals(bestMove.getFrom().getPiece().getOpposite(), bestMoveMirror.getFrom().getPiece());
        Assert.assertEquals(bestMove.getFrom().getSquare().getMirrorSquare(), bestMoveMirror.getFrom().getSquare());

        Assert.assertEquals(bestMove.getTo().getSquare().getMirrorSquare(), bestMoveMirror.getTo().getSquare());

    }

}
