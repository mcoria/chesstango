package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.Evaluator;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 * <p>
 * Esta es la suite de test unitarios a la cual se somete cada analizador estatico.
 * La comparacion deberia ser entre posiciones que difieren muy poco.
 * Al final del dia la comparacion entre dos evaluaciones estaticas provee un gradiente.
 * <p>
 * Respecto a cada feature (o termino en la sumatoria).
 * - El puntaje de cada termino es 0 en la posicion inicial
 * - El puntaje de cada termino es simetrico con respecto a la posicion, es decir si espejamos la posicion...
 * ver testEvaluateByMaterial()
 * <p>
 * https://www.chessprogramming.org/Evaluation_Philosophy
 * <p>
 * Miestras mas tiempo demore evaluando una posicion, menos tiempo hay para buscar, y por lo tanto
 * menor profundidad puede alcanzar la busqueda.
 */
public abstract class EvaluatorTestCollection {

    protected abstract AbstractEvaluator getEvaluator(Game game);


    protected void testGenericFeature(Evaluator evaluator, Supplier<Integer> evaluationFunction, String fen) {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        evaluator.setGame(game);
        final int eval = evaluationFunction.get();
        assertEquals(0, eval);

        game = FENDecoder.loadGame(fen);
        evaluator.setGame(game);
        final int eval1 = evaluationFunction.get();

        Game mirrorGame = game.mirror();
        evaluator.setGame(mirrorGame);
        final int eval2 = evaluationFunction.get();

        // El puntaje de cada termino es simetrico con respecto a la posicion
        assertEquals(eval1, -eval2);
    }

    @Test
    public void testInMateBlack() {
        Game mate = FENDecoder.loadGame("4Q2k/8/7K/8/8/8/8/8 b - - 0 1");       // Black is in Mate
        Game check = FENDecoder.loadGame("2q4k/8/7K/8/3Q4/8/8/8 b - - 0 1");    // Black is in Check
        Game draw = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");       // Draw

        int mateEval = getEvaluator(mate).evaluate();
        int checkEval = getEvaluator(check).evaluate();
        int drawEval = getEvaluator(draw).evaluate();

        // White's interest is to maximize
        // Black's interest is to minimize

        assertEquals(Evaluator.BLACK_LOST, mateEval);

        assertEquals(Evaluator.WHITE_WON, mateEval);

        assertTrue(mateEval > checkEval);

        assertTrue(mateEval > drawEval);
    }


    @Test
    public void testInMateWhite() {
        Game mate = FENDecoder.loadGame("8/8/8/8/8/7k/8/4q2K w - - 0 1");        // White is in Mate
        Game check = FENDecoder.loadGame("7k/8/8/3q4/8/8/8/2Q4K w - - 0 1");     // White is in Check
        Game draw = FENDecoder.loadGame("6q1/8/8/8/8/7k/8/7K w - - 0 1");         // Draw

        int mateEval = getEvaluator(mate).evaluate();
        int checkEval = getEvaluator(check).evaluate();
        int drawEval = getEvaluator(draw).evaluate();

        // White's interest is to maximize
        // Black's interest is to minimize

        assertEquals(Evaluator.BLACK_WON, mateEval);

        assertEquals(Evaluator.WHITE_LOST, mateEval);

        assertTrue(mateEval < checkEval);

        assertTrue(mateEval < drawEval);
    }


    @Test
    public void testCloseToPromotionOneMove() {
        Game promotionInOneMoves = FENDecoder.loadGame("7k/P7/8/8/8/8/8/7K w - - 0 1 ");
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");


        int evalPromotionInOneMoves = getEvaluator(promotionInOneMoves).evaluate();
        int evalPromotionInTwoMoves = getEvaluator(promotionInTwoMoves).evaluate();

        assertTrue(evalPromotionInOneMoves > evalPromotionInTwoMoves, "Promotion in 1 move is better than promotion in 2 moves");
    }


    @Test
    public void testCloseToPromotionTwoMoves() {
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");
        Game promotionInThreeMoves = FENDecoder.loadGame("7k/8/8/P7/8/8/8/7K w - - 0 1");

        int evalPromotionInTwoMoves = getEvaluator(promotionInTwoMoves).evaluate();
        int evalPromotionInThreeMoves = getEvaluator(promotionInThreeMoves).evaluate();

        assertTrue(evalPromotionInTwoMoves > evalPromotionInThreeMoves, "Promotion in 2 move is better than promotion 3 moves");
    }

    @Test
    public void testComparatives() {
        Game game = FENDecoder.loadGame("1k6/3Q4/6P1/1pP5/8/1B3P2/3R4/6K1 w - - 0 1");

        int eval = getEvaluator(game).evaluate();

        assertTrue(eval != Evaluator.WHITE_WON, "White has not won yet");
        assertTrue(eval != Evaluator.WHITE_LOST, "White has not lost yet");
        assertTrue(eval != Evaluator.BLACK_WON, "Black has not won yet");
        assertTrue(eval != Evaluator.BLACK_LOST, "Black has not lost yet");
        assertTrue(eval > 0, "White has a better position than Black");
    }

    @Test
    public void testDraw() {
        Game game = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");

        int eval = getEvaluator(game).evaluate();

        assertEquals(0, eval);
    }


    @Test
    public void testSymmetryOfGame() {
        Game game = FENDecoder.loadGame("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7");
        Game gameMirror = game.mirror();

        assertEquals(getEvaluator(game).evaluate(), (-1) * getEvaluator(gameMirror).evaluate());
    }

    @Test
    public void testInfinities() {
        assertEquals(Evaluator.INFINITE_POSITIVE, (-1) * Evaluator.INFINITE_NEGATIVE, "+infinite is equals to  (-1) * -infinite ");
        assertEquals(Evaluator.INFINITE_NEGATIVE, (-1) * Evaluator.INFINITE_POSITIVE, "-infinite is equals to  (-1) * +infinite ");

        assertEquals(Evaluator.BLACK_LOST, Evaluator.WHITE_WON);
        assertEquals(Evaluator.BLACK_WON, Evaluator.WHITE_LOST);

        assertEquals(Evaluator.WHITE_WON, Evaluator.BLACK_LOST);
        assertEquals(Evaluator.WHITE_LOST, Evaluator.BLACK_WON);

        assertEquals(Evaluator.WHITE_WON, (-1) * Evaluator.WHITE_LOST);
        assertEquals(Evaluator.BLACK_WON, (-1) * Evaluator.BLACK_LOST);

        assertEquals(Evaluator.WHITE_LOST, (-1) * Evaluator.WHITE_WON);
        assertEquals(Evaluator.BLACK_LOST, (-1) * Evaluator.BLACK_WON);
    }

}
