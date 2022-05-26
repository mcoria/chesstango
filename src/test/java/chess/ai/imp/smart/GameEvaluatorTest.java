package chess.ai.imp.smart;

import chess.board.Game;
import chess.board.builder.imp.GameBuilder;
import chess.board.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GameEvaluatorTest {

    private GameEvaluator evaluator;

    @Before
    public void setUp() {
        evaluator = new GameEvaluator();
    }

    @Test
    public void testQueenWhiteCheckMate() {
        Game game = getGame("rnbqkbnr/2pppQpp/8/pp4N1/8/4P3/PPPP1PPP/RNB1KB1R b KQkq - 0 5");

        int depth1 = evaluator.evaluate(game, 1);

        int depth3 = evaluator.evaluate(game, 3);

        Assert.assertTrue(depth1 > depth3);
    }

    protected Game getGame(String string) {
        GameBuilder builder = new GameBuilder();

        FENDecoder parser = new FENDecoder(builder);

        parser.parseFEN(string);

        return builder.getResult();
    }
}
