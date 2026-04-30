package net.chesstango.search.smart.alphabeta.pv;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByFEN;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.pv.model.TriangularPVTable;
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

    @Test
    public void test_beforeSearch() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);

        // Supongamos que se ejecutó walkPrincipalVariation
        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);
        pvCalculator.setPrincipalVariation(List.of(new PrincipalVariation(startZobrist, startExecutedMove)));
        pvCalculator.setPvComplete(true);

        // Y continuamos con la siguiente busqueda
        pvCalculator.beforeSearch();

        assertFalse(pvCalculator.isPvComplete());
        assertNull(pvCalculator.getPrincipalVariation());
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
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", 10);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.clearPV(0);
        pvTable.clearPV(1);
        pvTable.updatePV(0, (short) 0x0195);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(1, pv.size());

        List<String> pvString = pv.stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"g1f3"}, pvString.toArray());

        assertTrue(pvCalculator.isPvComplete());

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
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkb1r/pppppppp/5n2/8/8/5N2/PPPPPPPP/RNBQKB1R w KQkq - 2 2", 10);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.clearPV(0);
        pvTable.clearPV(1);
        pvTable.clearPV(2);
        pvTable.updatePV(1, (short) 0x0FAD);
        pvTable.updatePV(0, (short) 0x0195);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(2, pv.size());

        List<String> pvString = pv.stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"g1f3", "g8f6"}, pvString.toArray());

        assertTrue(pvCalculator.isPvComplete());

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
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkb1r/pppppppp/5n2/8/3P4/5N2/PPP1PPPP/RNBQKB1R b KQkq d3 0 2", 10);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.clearPV(0);
        pvTable.clearPV(1);
        pvTable.clearPV(2);
        pvTable.clearPV(3);
        pvTable.updatePV(2, (short) 0x02DB);
        pvTable.updatePV(1, (short) 0x0FAD);
        pvTable.updatePV(0, (short) 0x0195);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        pvCalculator.calculatePrincipalVariation(10);

        /**
         * Assertions
         */
        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(3, pv.size());

        List<String> pvString = pv.stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"g1f3", "g8f6", "d2d4"}, pvString.toArray());

        assertTrue(pvCalculator.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    @Test
    public void test_calculatePrincipalVariation_depth01_EGTB() {
        game = Game.from(FEN.from("4k3/8/8/5p2/6P1/2N5/8/4K3 w - - 0 1"));
        pvCalculator.setGame(game);
        pvCalculator.beforeSearch();

        evaluator.setGame(game);

        when(endGameTableBase.isProbeAvailable()).thenReturn(true);
        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        pvTable.clearPV(0);
        pvTable.clearPV(1);
        pvTable.updatePV(0, (short) 0x07A5);

        /**
         * Execute
         * Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
         */
        pvCalculator.calculatePrincipalVariation(Evaluator.WON);

        /**
         * Assertions
         */
        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(1, pv.size());

        List<String> pvString = pv.stream().map(PrincipalVariation::move).map(SimpleMoveEncoder.INSTANCE::encode).toList();
        assertArrayEquals(new String[]{"g4f5"}, pvString.toArray());

        assertTrue(pvCalculator.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

}
