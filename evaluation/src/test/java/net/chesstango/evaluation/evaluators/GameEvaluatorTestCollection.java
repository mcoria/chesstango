package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluator;
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
public abstract class GameEvaluatorTestCollection {

    protected abstract AbstractEvaluator getEvaluator(Game game);


    protected void testGenericFeature(GameEvaluator gameEvaluator, Supplier<Integer> evaluationFunction, String fen) {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        gameEvaluator.setGame(game);
        final int eval = evaluationFunction.get();
        assertEquals(0, eval);

        game = FENDecoder.loadGame(fen);
        gameEvaluator.setGame(game);
        final int eval1 = evaluationFunction.get();

        Game mirrorGame = game.mirror();
        gameEvaluator.setGame(mirrorGame);
        final int eval2 = evaluationFunction.get();

        // El puntaje de cada termino es simetrico con respecto a la posicion
        assertTrue(eval1 == -eval2);
    }

    @Test
    public void testEvaluateByMaterial() {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        final int eval = getEvaluator(game).evaluateByMaterial();
        assertEquals(0, eval);

        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        final int evalWhite = getEvaluator(game).evaluateByMaterial();
        assertTrue(evalWhite > 0);

        game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        final int evalBlack = getEvaluator(game).evaluateByMaterial();
        assertTrue(evalBlack < 0);

        // El puntaje de cada termino es simetrico con respecto a la posicion
        assertTrue(evalWhite == -evalBlack);
    }

    @Test
    public void testEvaluateByMaterial01() {
        AbstractEvaluator evaluator = getEvaluator(null);
        testGenericFeature(evaluator, evaluator::evaluateByMaterial, "rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
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

        assertEquals(GameEvaluator.BLACK_LOST, mateEval);

        assertEquals(GameEvaluator.WHITE_WON, mateEval);

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

        assertEquals(GameEvaluator.BLACK_WON, mateEval);

        assertEquals(GameEvaluator.WHITE_LOST, mateEval);

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

        assertTrue(eval != GameEvaluator.WHITE_WON, "White has not won yet");
        assertTrue(eval != GameEvaluator.WHITE_LOST, "White has not lost yet");
        assertTrue(eval != GameEvaluator.BLACK_WON, "Black has not won yet");
        assertTrue(eval != GameEvaluator.BLACK_LOST, "Black has not lost yet");
        assertTrue(eval > 0, "White has a better position than Black");
    }

    @Test
    public void testDraw() {
        Game game = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");

        int eval = getEvaluator(game).evaluate();

        assertEquals(0, eval);
    }

    @Test
    public void testSymmetryOfPieceValues() {
        AbstractEvaluator evaluator = getEvaluator(null);
        assertTrue(evaluator.getPieceValue(Piece.PAWN_WHITE) == -evaluator.getPieceValue(Piece.PAWN_BLACK));
        assertTrue(evaluator.getPieceValue(Piece.ROOK_WHITE) == -evaluator.getPieceValue(Piece.ROOK_BLACK));
        assertTrue(evaluator.getPieceValue(Piece.KNIGHT_WHITE) == -evaluator.getPieceValue(Piece.KNIGHT_BLACK));
        assertTrue(evaluator.getPieceValue(Piece.BISHOP_WHITE) == -evaluator.getPieceValue(Piece.BISHOP_BLACK));
        assertTrue(evaluator.getPieceValue(Piece.QUEEN_WHITE) == -evaluator.getPieceValue(Piece.QUEEN_BLACK));
        assertTrue(evaluator.getPieceValue(Piece.KING_WHITE) == -evaluator.getPieceValue(Piece.KING_BLACK));
    }


    @Test
    public void testSymmetryOfGame() {
        Game game = FENDecoder.loadGame("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7");
        Game gameMirror = game.mirror();

        assertTrue(getEvaluator(game).evaluate() == (-1) * getEvaluator(gameMirror).evaluate());
    }

    @Test
    public void testInfinities() {
        assertEquals(GameEvaluator.INFINITE_POSITIVE, (-1) * GameEvaluator.INFINITE_NEGATIVE, "+infinite is equals to  (-1) * -infinite ");
        assertEquals(GameEvaluator.INFINITE_NEGATIVE, (-1) * GameEvaluator.INFINITE_POSITIVE, "-infinite is equals to  (-1) * +infinite ");

        assertEquals(GameEvaluator.BLACK_LOST, GameEvaluator.WHITE_WON);
        assertEquals(GameEvaluator.BLACK_WON, GameEvaluator.WHITE_LOST);

        assertEquals(GameEvaluator.WHITE_WON, GameEvaluator.BLACK_LOST);
        assertEquals(GameEvaluator.WHITE_LOST, GameEvaluator.BLACK_WON);

        assertEquals(GameEvaluator.WHITE_WON, (-1) * GameEvaluator.WHITE_LOST);
        assertEquals(GameEvaluator.BLACK_WON, (-1) * GameEvaluator.BLACK_LOST);

        assertEquals(GameEvaluator.WHITE_LOST, (-1) * GameEvaluator.WHITE_WON);
        assertEquals(GameEvaluator.BLACK_LOST, (-1) * GameEvaluator.BLACK_WON);
    }

    @Test
    public void testMaterial() {
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        int eval = getEvaluator(game).evaluateByMaterial();
        assertEquals(0, eval);

        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        eval = getEvaluator(game).evaluateByMaterial();
        assertTrue(eval > 0);


        game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        eval = getEvaluator(game).evaluateByMaterial();
        assertTrue(eval < 0);
    }

}
