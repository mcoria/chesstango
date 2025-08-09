package net.chesstango.evaluation.evaluators;

import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.gardel.fen.FENParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static net.chesstango.evaluation.evaluators.EvaluatorImp04.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Mauricio Coria
 */
public class EvaluatorImp04Test extends EvaluatorTestCollection {

    private EvaluatorImp04 evaluator;

    @BeforeEach
    public void setUp() {
        evaluator = new EvaluatorImp04();
    }

    @Override
    protected AbstractEvaluator getEvaluator(Game game) {
        if (game != null) {
            evaluator.setGame(game);
        }
        return evaluator;
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
    public void testPawnValues() {
        assertEquals(5, PAWN_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(-20, PAWN_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(-20, PAWN_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(10, PAWN_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(5, PAWN_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(50, PAWN_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(50, PAWN_WHITE_VALUES[Square.h7.idx()]);

        assertEquals(64, PAWN_WHITE_VALUES.length);
        assertEquals(64, PAWN_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            assertEquals(PAWN_WHITE_VALUES[square.idx()], -PAWN_BLACK_VALUES[square.mirror().idx()]);
        }
    }

    @Test
    public void testKnightValues() {
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(5, KNIGHT_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(5, KNIGHT_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(0, KNIGHT_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(-20, KNIGHT_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(-40, KNIGHT_WHITE_VALUES[Square.h7.idx()]);

        assertEquals(64, KNIGHT_WHITE_VALUES.length);
        assertEquals(64, KNIGHT_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, KNIGHT_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), KNIGHT_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(KNIGHT_WHITE_VALUES[square.idx()], -KNIGHT_BLACK_VALUES[square.mirror().idx()]);
        }

    }

    @Test
    public void testBishopValues() {
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(5, BISHOP_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(5, BISHOP_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(-10, BISHOP_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(0, BISHOP_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(-10, BISHOP_WHITE_VALUES[Square.h7.idx()]);

        assertEquals(64, BISHOP_WHITE_VALUES.length);
        assertEquals(64, BISHOP_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, BISHOPS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), BISHOPS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(BISHOP_WHITE_VALUES[square.idx()], -BISHOP_BLACK_VALUES[square.mirror().idx()]);
        }
    }

    @Test
    public void testRookValues() {
        assertEquals(-5, ROOK_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(0, ROOK_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(-5, ROOK_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(5, ROOK_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(10, ROOK_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(5, ROOK_WHITE_VALUES[Square.h7.idx()]);

        assertEquals(64, ROOK_WHITE_VALUES.length);
        assertEquals(64, ROOK_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, ROOKS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), ROOKS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(ROOK_WHITE_VALUES[square.idx()], -ROOK_BLACK_VALUES[square.mirror().idx()]);
        }
    }


    @Test
    public void testQueenValues() {
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(5, QUEEN_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(-10, QUEEN_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(0, QUEEN_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(-10, QUEEN_WHITE_VALUES[Square.h7.idx()]);


        assertEquals(64, QUEEN_WHITE_VALUES.length);
        assertEquals(64, QUEEN_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, QUEENS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), QUEENS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(QUEEN_WHITE_VALUES[square.idx()], -QUEEN_BLACK_VALUES[square.mirror().idx()]);
        }
    }

    @Test
    public void testKingValues() {
        assertEquals(20, KING_WHITE_VALUES[Square.a2.idx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.b2.idx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.c2.idx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.d2.idx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.e2.idx()]);
        assertEquals(0, KING_WHITE_VALUES[Square.f2.idx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.g2.idx()]);
        assertEquals(20, KING_WHITE_VALUES[Square.h2.idx()]);

        assertEquals(-30, KING_WHITE_VALUES[Square.a7.idx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.b7.idx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.c7.idx()]);
        assertEquals(-50, KING_WHITE_VALUES[Square.d7.idx()]);
        assertEquals(-50, KING_WHITE_VALUES[Square.e7.idx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.f7.idx()]);
        assertEquals(-40, KING_WHITE_VALUES[Square.g7.idx()]);
        assertEquals(-30, KING_WHITE_VALUES[Square.h7.idx()]);

        /*
        for (int rank = 0; rank < 8; rank++) {
            for (int file = 0; file < 8; file++) {
                System.out.printf("%d,", -KING_WHITE_VALUES[Square.getSquare(file, rank).getMirrorSquare().toIdx()]);
                if (file == 7) {
                    System.out.println("\t // Rank " + (rank + 1));
                }
            }
        }
         */


        assertEquals(64, KING_WHITE_VALUES.length);
        assertEquals(64, KING_BLACK_VALUES.length);

        for (Square square : Square.values()) {
            //System.out.printf("Comparing %s(%d) and %s(%d)\n", square, ROOKS_WHITE_VALUES[square.toIdx()], square.getMirrorSquare(), ROOKS_BLACK_VALUES[square.getMirrorSquare().toIdx()]);
            assertEquals(KING_WHITE_VALUES[square.idx()], -KING_BLACK_VALUES[square.mirror().idx()]);
        }
    }

    @Test
    public void testEvaluateByPosition() {
        Game game = Game.from(FEN.of("r3kb1r/1p3ppp/p7/P1pp2n1/3n1R2/6q1/1PPPB1b1/RNBQ2K1 b kq - 1 21"));
        evaluator.setGame(game);
        assertEquals(-90, evaluator.evaluateByPosition());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(90, evaluator.evaluateByPosition());
    }

    @Test
    public void testEvaluateSymmetric01() {
        Game game = Game.from(FEN.of("r1bqkb1r/ppp1pppp/2n2n2/3p4/3P4/2N2N2/PPP1PPPP/R1BQKB1R w KQkq d6 0 4"));
        evaluator.setGame(game);
        assertEquals(0, evaluator.evaluate());

        Game gameMirror = game.mirror();
        evaluator.setGame(gameMirror);
        assertEquals(0, evaluator.evaluateByPosition());
    }


    @Test
    public void testEvaluation01() {
        Game game = Game.from(FEN.of("7k/6p1/8/8/8/N7/8/K7 w - - 0 1"));
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c4);
        game.executeMove(Square.h8, Square.g8);

        assertEquals(165300, evaluator.evaluate());
    }

    @Test
    public void testEvaluation02() {
        Game game = Game.from(FEN.of("7k/6p1/8/8/8/N7/8/K7 w - - 0 1"));
        evaluator.setGame(game);

        game.executeMove(Square.a3, Square.c2);
        game.executeMove(Square.h8, Square.g8);
        game.executeMove(Square.c2, Square.d4);


        assertEquals(168750, evaluator.evaluate());
    }

    @Test
    public void testEvaluation03() {
        Game game = Game.from(FEN.of("r2qnrnk/p2b2b1/1p1p2pp/2pPpp2/1PP1P3/PRNBB3/3QNPPP/5RK1 w - - 0 1"));
        evaluator.setGame(game);

        game.executeMove(Square.f2, Square.f4);
        game.executeMove(Square.c5, Square.b4);
        game.executeMove(Square.b3, Square.b4);
        game.executeMove(Square.d8, Square.e7);
        game.executeMove(Square.f4, Square.e5);
        game.executeMove(Square.d6, Square.e5);


        assertEquals(26000, evaluator.evaluate());
    }

    @Test
    public void testEvaluation04() {
        Game game = Game.from(FEN.of("r1b2rk1/2q1b1pp/p2ppn2/1p6/3QP3/1BN1B3/PPP3PP/R4RK1 w - - 0 1"));
        evaluator.setGame(game);

        game.executeMove(Square.c3, Square.d5);
        game.executeMove(Square.f6, Square.d5);
        game.executeMove(Square.f1, Square.f8);
        game.executeMove(Square.g8, Square.f8);
        game.executeMove(Square.e4, Square.d5);


        assertEquals(19515, evaluator.evaluate());
    }

    @Test
    public void testEvaluation05() {
        Game game = Game.from(FEN.of("1r4k1/pp3p1p/3p2pB/4r3/P7/1P1P1K2/3R2BP/8 w - - 0 31"));
        evaluator.setGame(game);


        assertEquals(-52230, evaluator.evaluate());
    }

    @Test
    public void testEvaluateByMaterial() {
        // El puntaje de cada termino es 0 en la posicion inicial
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));
        evaluator.setGame(game);
        final int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = Game.from(FEN.of("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        evaluator.setGame(game);
        final int evalWhite = evaluator.evaluateByMaterial();
        assertTrue(evalWhite > 0);

        game = Game.from(FEN.of("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1"));
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
        Game game = Game.from(FEN.of(FENParser.INITIAL_FEN));
        evaluator.setGame(game);
        int eval = evaluator.evaluateByMaterial();
        assertEquals(0, eval);

        game = Game.from(FEN.of("rnbqkbnr/pppp1ppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"));
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval > 0);


        game = Game.from(FEN.of("rnbqkbnr/pppppppp/8/8/8/8/PPPP1PPP/RNBQKBNR b KQkq - 0 1"));
        evaluator.setGame(game);
        eval = evaluator.evaluateByMaterial();
        assertTrue(eval < 0);
    }

}
