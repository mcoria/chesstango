package net.chesstango.evaluation.evaluators;


import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


/**
 * @author Mauricio Coria
 */
public class EvaluatorImp03Test extends EvaluatorTestCollection {

    private EvaluatorImp03 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp03();
    }

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        if (game != null) {
            evaluator.setGame(game);
        }
        return evaluator;
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
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);
        evaluator.setGame(game);
        final int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = Game.fromFEN("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        evaluator.setGame(game);
        final int evalWhite = evaluator.evaluateByMaterial();
        assertTrue(evalWhite > 0);

        game = Game.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
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
        Game game = Game.fromFEN(FENParser.INITIAL_FEN);
        evaluator.setGame(game);
        int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = Game.fromFEN("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval > 0);


        game = Game.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1");
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval < 0);
    }
}
