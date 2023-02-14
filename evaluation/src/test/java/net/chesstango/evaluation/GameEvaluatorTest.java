package net.chesstango.evaluation;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Test;

public class GameEvaluatorTest {

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

        Assert.assertEquals(0, GameEvaluator.evaluateByMaterial(game));
    }

    @Test
    public void testMaterial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int eval = GameEvaluator.evaluateByMaterial(game);
        Assert.assertEquals(0, eval);

        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1 ");
        eval = GameEvaluator.evaluateByMaterial(game);
        Assert.assertTrue(eval > 0);


        game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        eval = GameEvaluator.evaluateByMaterial(game);
        Assert.assertTrue(eval < 0);
    }
}