package net.chesstango.evaluation.imp;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


/**
 * @author Mauricio Coria
 */
public class GameEvaluatorImp01Test extends GameEvaluatorTestCollection {

    private GameEvaluatorImp01 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new GameEvaluatorImp01();
    }

    @Override
    protected AbstractEvaluator getEvaluator() {
        return evaluator;
    }

    @Test
    public void test_evaluateByMoves_white() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int eval1 = evaluator.evaluateByMoves(game);


        game = FENDecoder.loadGame("rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        int eval2 = evaluator.evaluateByMoves(game);

        assertTrue(eval1 > 0);
        assertTrue(eval2 > 0);

        assertTrue(eval2 > eval1);
    }


    @Test
    public void test_evaluateByMoves_black() {
        Game game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        int eval1 = evaluator.evaluateByMoves(game);


        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
        int eval2 = evaluator.evaluateByMoves(game);

        assertTrue(eval1 < 0);
        assertTrue(eval2 < 0);
        assertTrue(eval2 < eval1);
    }

    @Test
    @Disabled //Evidentemente no cumple las condiciones
    public void test_evaluateByMoves_generic() {
        testGenericFeature(evaluator::evaluateByMoves, "rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        testGenericFeature(evaluator::evaluateByMoves, "rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        testGenericFeature(evaluator::evaluateByMoves, "rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
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
