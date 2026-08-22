package net.chesstango.search.smart.pv.model;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByFEN;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PVMove;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.egtb.EndGameTableBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PVCalculatorTriangularTest {

    private EvaluatorByFEN evaluator;

    @Mock
    private EndGameTableBase endGameTableBase;

    private PVCalculatorTriangular pvCalculator;

    private TriangularPVTable pvTable;

    private Game game;

    @BeforeEach
    public void setup() {
        evaluator = new EvaluatorByFEN();
        pvTable = new TriangularPVTable();

        pvCalculator = new PVCalculatorTriangular();
        pvCalculator.setEvaluator(evaluator);
        pvCalculator.setEndGameTableBase(endGameTableBase);
        pvCalculator.setTrianglePV(pvTable);
    }


    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {g1f3}
     */
    @Test
    public void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", 10);

        /**
         * Secuencia de ejecucion
         */
        pvTable.extendLine(0, null);

        Move move = game.getMove(Square.g1, Square.f3);
        move.executeMove();

        pvTable.extendLine(1, move);

        pvTable.propagateLine(0);

        long zobristBeforeCalculate = game.getPosition().getZobristHash();
        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */

        PrincipalVariation pv = pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PVMove> pvMoves = pv.pvMoves();

        assertEquals(1, pvMoves.size());

        List<String> pvString = pvMoves.stream().map(PVMove::move).map(Move::coordinateEncoding).toList();
        assertArrayEquals(new String[]{"g1f3"}, pvString.toArray());

        assertTrue(pv.pvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 2
     * PV = {g1f3, d7d5}
     */
    @Test
    public void test_calculatePrincipalVariation_depth02() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkb1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 2 2", 10);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.extendLine(0, null);

        Move pvMove = game.getMove(Square.g1, Square.f3);
        pvMove.executeMove();
        pvTable.extendLine(1, pvMove);

        pvMove = game.getMove(Square.g8, Square.f6);
        pvMove.executeMove();
        pvTable.extendLine(2, pvMove);

        game.undoMove();
        pvTable.propagateLine(1);

        game.undoMove();
        pvTable.propagateLine(0);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        PrincipalVariation pv = pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PVMove> pvMoves = pv.pvMoves();

        assertEquals(2, pvMoves.size());

        List<String> pvString = pvMoves.stream().map(PVMove::move).map(Move::coordinateEncoding).toList();
        assertArrayEquals(new String[]{"g1f3", "g8f6"}, pvString.toArray());

        assertTrue(pv.pvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 2
     * PV = {g1f3, d7d5}
     */
    @Test
    public void test_calculatePrincipalVariation_depth03() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkb1r/pppppppp/5n2/8/3P4/5N2/PPP1PPPP/RNBQKB1R b KQkq d3 0 2", 10);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.extendLine(0, null);

        Move pvMove = game.getMove(Square.g1, Square.f3);
        pvMove.executeMove();
        pvTable.extendLine(1, pvMove);

        pvMove = game.getMove(Square.g8, Square.f6);
        pvMove.executeMove();
        pvTable.extendLine(2, pvMove);

        pvMove = game.getMove(Square.d2, Square.d4);
        pvMove.executeMove();
        pvTable.extendLine(3, pvMove);


        game.undoMove();
        pvTable.propagateLine(2);

        game.undoMove();
        pvTable.propagateLine(1);

        game.undoMove();
        pvTable.propagateLine(0);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        PrincipalVariation pv = pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PVMove> pvMoves = pv.pvMoves();

        assertEquals(3, pvMoves.size());

        List<String> pvString = pvMoves.stream().map(PVMove::move).map(Move::coordinateEncoding).toList();
        assertArrayEquals(new String[]{"g1f3", "g8f6", "d2d4"}, pvString.toArray());

        assertTrue(pv.pvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    @Test
    public void test_calculatePrincipalVariation_depth01_EGTB() {
        game = Game.from(FEN.from("4k3/8/8/5p2/6P1/2N5/8/4K3 w - - 0 1"));
        pvCalculator.setGame(game);

        evaluator.setGame(game);

        when(endGameTableBase.isProbeAvailable()).thenReturn(true);
        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.extendLine(0, null);

        Move pvMove = game.getMove(Square.g4, Square.f5);
        pvMove.executeMove();
        pvTable.extendLine(1, pvMove);


        game.undoMove();
        pvTable.propagateLine(0);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        PrincipalVariation pv = pvCalculator.calculatePrincipalVariation(Evaluator.WON);

        /**
         * Assertions
         */
        List<PVMove> pvMoves = pv.pvMoves();

        assertEquals(1, pvMoves.size());

        List<String> pvString = pvMoves.stream().map(PVMove::move).map(Move::coordinateEncoding).toList();
        assertArrayEquals(new String[]{"g4f5"}, pvString.toArray());

        assertTrue(pv.pvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

}
