package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.representations.fen.FENParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp01Test extends EvaluatorTestCollection {

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
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);
        evaluator.setGame(game);
        int eval1 = evaluator.evaluateByMoves();


        game = FENParser.loadGame("rnbqkbnr/p1pppppp/1p6/8/8/4P3/PPPP1PPP/RNBQKBNR w KQkq - 0 2");
        evaluator.setGame(game);
        int eval2 = evaluator.evaluateByMoves();

        assertTrue(eval1 > 0);
        assertTrue(eval2 > 0);

        assertTrue(eval2 > eval1);
    }


    @Test
    public void test_evaluateByMoves_black() {
        Game game = FENParser.loadGame("rnbqkbnr/pppppppp/8/8/8/4P3/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        evaluator.setGame(game);
        int eval1 = evaluator.evaluateByMoves();


        game = FENParser.loadGame("rnbqkbnr/pppp1ppp/4p3/8/8/4P2P/PPPP1PP1/RNBQKBNR b KQkq - 0 2");
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

    @Test
    public void testSymmetryOfPieceValues() {
        assertEquals(evaluator.getPieceValue(Piece.PAWN_WHITE), -evaluator.getPieceValue(Piece.PAWN_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.ROOK_WHITE), -evaluator.getPieceValue(Piece.ROOK_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.KNIGHT_WHITE), -evaluator.getPieceValue(Piece.KNIGHT_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.BISHOP_WHITE), -evaluator.getPieceValue(Piece.BISHOP_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.QUEEN_WHITE), -evaluator.getPieceValue(Piece.QUEEN_BLACK));
        assertEquals(evaluator.getPieceValue(Piece.KING_WHITE), -evaluator.getPieceValue(Piece.KING_BLACK));
    }

    @Test
    public void testEvaluateByMaterial() {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);
        evaluator.setGame(game);
        final int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = FENParser.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        evaluator.setGame(game);
        final int evalWhite = evaluator.evaluateByMaterial();
        assertTrue(evalWhite > 0);

        game = FENParser.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        evaluator.setGame(game);
        final int evalBlack = evaluator.evaluateByMaterial();
        assertTrue(evalBlack < 0);

        // El puntaje de cada termino es simetrico con respecto a la posicion
        assertEquals(evalWhite, -evalBlack);
    }

    @Test
    public void testEvaluateByMaterial01() {
        testGenericFeature(evaluator, evaluator::evaluateByMaterial, "rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
    }

    @Test
    public void testEvaluateByMaterial02() {
        Game game = FENParser.loadGame(FENParser.INITIAL_FEN);
        evaluator.setGame(game);
        int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = FENParser.loadGame("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval > 0);


        game = FENParser.loadGame("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval < 0);
    }
}
