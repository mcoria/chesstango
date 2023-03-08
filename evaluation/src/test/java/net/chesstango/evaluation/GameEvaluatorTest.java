package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.imp.GameEvaluatorImp02;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Mauricio Coria
 *
 */
public class GameEvaluatorTest {

    private GameEvaluator evaluator;

    @Before
    public void setup() {
        evaluator = new GameEvaluator() {

            @Override
            public int getPieceValue(Game game, Piece piece) {
                return switch (piece) {
                    case PAWN_WHITE -> 1;
                    case PAWN_BLACK -> -1;
                    case KNIGHT_WHITE -> 3;
                    case KNIGHT_BLACK -> -3;
                    case BISHOP_WHITE -> 3;
                    case BISHOP_BLACK -> -3;
                    case ROOK_WHITE -> 5;
                    case ROOK_BLACK -> -5;
                    case QUEEN_WHITE -> 9;
                    case QUEEN_BLACK -> -9;
                    case KING_WHITE -> 10;
                    case KING_BLACK -> -10;
                };
            }

            @Override
            public int evaluate(Game game) {
                return 0;
            }
        };
    }

    @Test
    public void testInfinities() {
        Assert.assertEquals("+infinite is equals to  (-1) * -infinite ", GameEvaluator.INFINITE_POSITIVE, (-1) * GameEvaluator.INFINITE_NEGATIVE);
        Assert.assertEquals("-infinite is equals to  (-1) * +infinite ", GameEvaluator.INFINITE_NEGATIVE, (-1) * GameEvaluator.INFINITE_POSITIVE);

        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.WHITE_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, GameEvaluator.BLACK_LOST);

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.BLACK_WON);
        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, GameEvaluator.WHITE_LOST);
    }

    @Test
    public void testEvaluateByMaterial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);

        Assert.assertEquals(0, evaluator.evaluateByMaterial(game));
    }

    @Test
    public void testMaterial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int eval = evaluator.evaluateByMaterial(game);
        Assert.assertEquals(0, eval);

        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        eval = evaluator.evaluateByMaterial(game);
        Assert.assertTrue(eval > 0);


        game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        eval = evaluator.evaluateByMaterial(game);
        Assert.assertTrue(eval < 0);
    }
}
