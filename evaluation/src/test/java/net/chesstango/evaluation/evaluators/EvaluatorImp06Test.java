package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorImp05.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp06Test extends EvaluatorTestCollection {

    private EvaluatorImp06 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp06();
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

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        if (game != null) {
            evaluator.setGame(game);
        }
        return evaluator;
    }

    @Test
    public void testPawnValues() {
        assertEquals(64, MG_PAWN_TBL.length);
        assertEquals(64, EG_PAWN_TBL.length);
    }

    @Test
    public void testKnightValues() {
        assertEquals(64, MG_KNIGHT_TBL.length);
        assertEquals(64, EG_KNIGHT_TBL.length);
    }

    @Test
    public void testBishopValues() {
        assertEquals(64, MG_BISHOP_TBL.length);
        assertEquals(64, EG_BISHOP_TBL.length);
    }

    @Test
    public void testRookValues() {
        assertEquals(64, MG_ROOK_TBL.length);
        assertEquals(64, EG_ROOK_TBL.length);
    }


    @Test
    public void testQueenValues() {
        assertEquals(64, MG_QUEEN_TBL.length);
        assertEquals(64, EG_QUEEN_TBL.length);
    }

    @Test
    public void testKingValues() {
        assertEquals(64, MG_KING_TBL.length);
        assertEquals(64, EG_KING_TBL.length);
    }

    @Test
    public void testEvaluateByPosition() {
        Game game = Game.fromFEN("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21");
        evaluator.setGame(game);
        assertEquals(-218262, evaluator.evaluateByPST());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(218262, evaluator.evaluateByPST());
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = Game.fromFEN("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4");
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(0, evaluator.evaluateByPST());
    }


    @Test
    public void testEvaluation01() {
        Game game = Game.fromFEN("7k/6p1/8/8/8/N7/8/K7 w - - 0 1");
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c4);
        game.executeMove(Square.h8, Square.g8);

        assertEquals(174890, evaluator.evaluate());
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
