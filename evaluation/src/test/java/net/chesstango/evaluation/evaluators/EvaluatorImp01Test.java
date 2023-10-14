package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp01Test extends GameEvaluatorTestCollection {

    private EvaluatorImp01 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp01();
    }

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        evaluator.setGame(game);
        return evaluator;
    }

    @Test
    public void test_evaluateByMoves_white() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        evaluator.setGame(game);
        int eval1 = evaluator.evaluateByMoves();


        game = FENDecoder.loadGame("rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        evaluator.setGame(game);
        int eval2 = evaluator.evaluateByMoves();

        assertTrue(eval1 > 0);
        assertTrue(eval2 > 0);

        assertTrue(eval2 > eval1);
    }


    @Test
    public void test_evaluateByMoves_black() {
        Game game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        evaluator.setGame(game);
        int eval1 = evaluator.evaluateByMoves();


        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
        evaluator.setGame(game);
        int eval2 = evaluator.evaluateByMoves();

        assertTrue(eval1 < 0);
        assertTrue(eval2 < 0);
        assertTrue(eval2 < eval1);
    }

    @Test
    @Disabled //Evidentemente no cumple las condiciones
    public void test_evaluateByMoves_generic() {
        testGenericFeature(evaluator, evaluator::evaluateByMoves, "rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        testGenericFeature(evaluator, evaluator::evaluateByMoves, "rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        testGenericFeature(evaluator, evaluator::evaluateByMoves, "rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionOneMove() {
    }

    @Test
    @Override
    @Disabled //El evaluator no es lo suficientemente bueno como para resolver esta situation
    public void testCloseToPromotionTwoMoves() {
    }
}
