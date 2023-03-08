package net.chesstango.evaluation.imp;

import net.chesstango.board.Piece;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.builders.GameBuilder;
import net.chesstango.board.builders.MirrorBuilder;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.evaluation.GameEvaluator;
import net.chesstango.board.Game;
import net.chesstango.board.representations.fen.FENDecoder;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.function.Function;

/**
 * @author Mauricio Coria
 *
 * Esta es la suite de test unitarios a la cual se somete cada analizador estatico.
 * La comparacion deberia ser entre posiciones que difieren muy poco.
 * Al final del dia la comparacion entre dos evaluaciones estaticas provee un gradiente.
 *
 * Respecto a cada feature (o termino en la sumatoria).
 * - El puntaje de cada termino es 0 en la posicion inicial
 * - El puntaje de cada termino es simetrico con respecto a la posicion, es decir si espejamos la posicion...
 * ver testEvaluateByMaterial()
 *
 * https://www.chessprogramming.org/Evaluation_Philosophy
 *
 * Miestras mas tiempo demore evaluando una posicion, menos tiempo hay para buscar, y por lo tanto
 * menor profundidad puede alcanzar la busqueda.
 */
public abstract class GameEvaluationTestCollection {

    protected abstract GameEvaluator getEvaluator();


    protected void testGenericFeature(Function<Game, Integer> evaluationFunction, String fen){
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        final int eval = evaluationFunction.apply(game);
        Assert.assertEquals(0, eval);

        game = FENDecoder.loadGame(fen);
        final int eval1 = evaluationFunction.apply(game);

        MirrorBuilder<Game> mirrorBuilder = new MirrorBuilder(new GameBuilder());
        game.getChessPosition().constructBoardRepresentation(mirrorBuilder);
        Game mirrorGame = mirrorBuilder.getChessRepresentation();
        final int eval2 = evaluationFunction.apply(mirrorGame);

        // El puntaje de cada termino es simetrico con respecto a la posicion
        Assert.assertTrue(eval1 == - eval2);
    }

    @Test
    public void testEvaluateByMaterial() {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENDecoder.loadGame(FENDecoder.INITIAL_FEN);
        final int eval = getEvaluator().evaluateByMaterial(game);
        Assert.assertEquals(0, eval);

        game = FENDecoder.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        final int evalWhite = getEvaluator().evaluateByMaterial(game);
        Assert.assertTrue(evalWhite > 0);

        game = FENDecoder.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        final int evalBlack = getEvaluator().evaluateByMaterial(game);
        Assert.assertTrue(evalBlack < 0);

        // El puntaje de cada termino es simetrico con respecto a la posicion
        Assert.assertTrue(evalWhite == - evalBlack);
    }

    @Test
    public void testEvaluateByMaterial01() {
        testGenericFeature(getEvaluator()::evaluateByMaterial, "rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void testInMateBlack() {
        Game mate = FENDecoder.loadGame("4Q2k/8/7K/8/8/8/8/8 b - - 0 1");       // Black is in Mate
        Game check = FENDecoder.loadGame("2q4k/8/7K/8/3Q4/8/8/8 b - - 0 1");    // Black is in Check
        Game draw = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");       // Draw

        int mateEval = getEvaluator().evaluate(mate);
        int checkEval = getEvaluator().evaluate(check);
        int drawEval = getEvaluator().evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize
        Assert.assertEquals(GameEvaluator.INFINITE_POSITIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_LOST, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_WON, mateEval);

        Assert.assertTrue(mateEval > checkEval);

        Assert.assertTrue(mateEval > drawEval);
    }


    @Test
    public void testInMateWhite() {
        Game mate = FENDecoder.loadGame("8/8/8/8/8/7k/8/4q2K w - - 0 1");        // White is in Mate
        Game check = FENDecoder.loadGame("7k/8/8/3q4/8/8/8/2Q4K w - - 0 1");     // White is in Check
        Game draw = FENDecoder.loadGame("6q1/8/8/8/8/7k/8/7K w - - 0 1");         // Draw

        int mateEval = getEvaluator().evaluate(mate);
        int checkEval = getEvaluator().evaluate(check);
        int drawEval = getEvaluator().evaluate(draw);

        // White's interest is to maximize
        // Black's interest is to minimize

        Assert.assertEquals(GameEvaluator.INFINITE_NEGATIVE, mateEval);

        Assert.assertEquals(GameEvaluator.BLACK_WON, mateEval);

        Assert.assertEquals(GameEvaluator.WHITE_LOST, mateEval);

        Assert.assertTrue(mateEval < checkEval);

        Assert.assertTrue(mateEval < drawEval);
    }


    @Test
    public void testCloseToPromotionOneMove() {
        Game promotionInOneMoves = FENDecoder.loadGame("7k/P7/8/8/8/8/8/7K w - - 0 1 ");
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");


        int evalPromotionInOneMoves = getEvaluator().evaluate(promotionInOneMoves);
        int evalPromotionInTwoMoves = getEvaluator().evaluate(promotionInTwoMoves);

        Assert.assertTrue("Promotion in 1 move is better than promotion in 2 moves", evalPromotionInOneMoves > evalPromotionInTwoMoves);
    }


    @Test
    public void testCloseToPromotionTwoMoves() {
        Game promotionInTwoMoves = FENDecoder.loadGame("7k/8/P7/8/8/8/8/7K w - - 0 1");
        Game promotionInThreeMoves = FENDecoder.loadGame("7k/8/8/P7/8/8/8/7K w - - 0 1");

        int evalPromotionInTwoMoves = getEvaluator().evaluate(promotionInTwoMoves);
        int evalPromotionInThreeMoves = getEvaluator().evaluate(promotionInThreeMoves);

        Assert.assertTrue("Promotion in 2 move is better than promotion 3 moves", evalPromotionInTwoMoves > evalPromotionInThreeMoves);
    }

    @Test
    public void testComparatives() {
        Game game = FENDecoder.loadGame("1k6/3Q4/6P1/1pP5/8/1B3P2/3R4/6K1 w - - 0 1");

        int eval = getEvaluator().evaluate(game);

        Assert.assertTrue("White has not won yet", eval != GameEvaluator.WHITE_WON);
        Assert.assertTrue("White has not lost yet", eval != GameEvaluator.WHITE_LOST);
        Assert.assertTrue("Black has not won yet", eval != GameEvaluator.BLACK_WON);
        Assert.assertTrue("Black has not lost yet", eval != GameEvaluator.BLACK_LOST);
        Assert.assertTrue("White has a better position than Black", eval > 0);
    }

    @Test
    public void testDraw() {
        Game game = FENDecoder.loadGame("7k/8/7K/8/8/8/8/6Q1 b - - 0 1");

        int eval = getEvaluator().evaluate(game);

        Assert.assertEquals("Draw", 0, eval);
    }

    @Test
    public void testSymmetryOfPieceValues() {
        GameEvaluator evaluator = getEvaluator();
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.PAWN_WHITE) == -evaluator.getPieceValue(null, Piece.PAWN_BLACK)) ;
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.ROOK_WHITE) == -evaluator.getPieceValue(null, Piece.ROOK_BLACK)) ;
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.KNIGHT_WHITE) == -evaluator.getPieceValue(null, Piece.KNIGHT_BLACK)) ;
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.BISHOP_WHITE) == -evaluator.getPieceValue(null, Piece.BISHOP_BLACK)) ;
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.QUEEN_WHITE) == -evaluator.getPieceValue(null, Piece.QUEEN_BLACK)) ;
        Assert.assertTrue(evaluator.getPieceValue(null, Piece.KING_WHITE) == -evaluator.getPieceValue(null, Piece.KING_BLACK)) ;
    }


    @Test
    public void testSymmetryOfGame() {
        Game game = FENDecoder.loadGame ("r1bqkb1r/pp3ppp/2nppn2/1N6/2P1P3/2N5/PP3PPP/R1BQKB1R b KQkq - 2 7");
        MirrorBuilder<Game> mirrorBuilder = new MirrorBuilder(new GameBuilder());
        game.getChessPosition().constructBoardRepresentation(mirrorBuilder);
        Game gameMirror = mirrorBuilder.getChessRepresentation();

        Assert.assertTrue(getEvaluator().evaluate(game) == (-1) * getEvaluator().evaluate(gameMirror) );

    }

}
