package net.chesstango.search.smart.alphabeta.pv;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
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

    @Mock
    private Evaluator evaluator;

    @Mock
    private EndGameTableBase endGameTableBase;

    private PVCalculatorTriangular PVCalculatorTriangular;

    private Game game;

    @BeforeEach
    public void setup() {
        PVCalculatorTriangular = new PVCalculatorTriangular();

        PVCalculatorTriangular.setEvaluator(evaluator);
        PVCalculatorTriangular.setEndGameTableBase(endGameTableBase);
        PVCalculatorTriangular.setTrianglePV(new short[40][40]);
    }

    @Test
    void test_beforeSearch() {
        game = Game.from(FEN.START_POSITION);
        PVCalculatorTriangular.setGame(game);
        PVCalculatorTriangular.setDepth(1);
        PVCalculatorTriangular.beforeSearch();
        PVCalculatorTriangular.beforeSearchByDepth();

        assertFalse(PVCalculatorTriangular.isPvComplete());
        assertNull(PVCalculatorTriangular.getPrincipalVariation());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {a2a4}
     */
    @Test
    void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        PVCalculatorTriangular.setGame(game);
        PVCalculatorTriangular.setDepth(1);
        PVCalculatorTriangular.beforeSearch();
        PVCalculatorTriangular.beforeSearchByDepth();

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        startExecutedMove.executeMove();

        final long nextZobrist = game.getPosition().getZobristHash();

        final short bestMove = 0;
        final int bestValue = 10;

        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        PVCalculatorTriangular.calculatePrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = PVCalculatorTriangular.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        assertTrue(PVCalculatorTriangular.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(nextZobrist, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 2
     * PV = {a2a4, g7g5}
     */
    @Test
    void test_calculatePrincipalVariation_depth02() {
        game = Game.from(FEN.START_POSITION);
        PVCalculatorTriangular.setGame(game);
        PVCalculatorTriangular.setDepth(2);
        PVCalculatorTriangular.beforeSearch();
        PVCalculatorTriangular.beforeSearchByDepth();

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        startExecutedMove.executeMove();
        final long nextZobrist = game.getPosition().getZobristHash();
        final Move nextExecutedMove = game.getMove(Square.g7, Square.g5);


        final short bestMove = nextExecutedMove.binaryEncoding();
        final int bestValue = 10;
        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        PVCalculatorTriangular.calculatePrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = PVCalculatorTriangular.getPrincipalVariation();

        assertEquals(2, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        PrincipalVariation lastPV = pv.getLast();
        assertEquals(nextZobrist, lastPV.hash());
        assertEquals(nextExecutedMove, lastPV.move());

        assertTrue(PVCalculatorTriangular.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(nextZobrist, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {a2a4}
     */
    @Test
    void test_beforeSearchByDepth() {
        game = Game.from(FEN.START_POSITION);
        PVCalculatorTriangular.setGame(game);
        PVCalculatorTriangular.setDepth(2);

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        // Supongamos que se ejecutó calculatePrincipalVariation
        PVCalculatorTriangular.setPrincipalVariation(List.of(new PrincipalVariation(startZobrist, startExecutedMove)));
        PVCalculatorTriangular.setPvComplete(true);

        // Y continuamos con la siguiente profundidad
        PVCalculatorTriangular.beforeSearchByDepth();

        // Entonces pvComplete debe ser false y PV null
        assertFalse(PVCalculatorTriangular.isPvComplete());
        assertNull(PVCalculatorTriangular.getPrincipalVariation());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {a2a4}
     */
    @Test
    void test_calculatePrincipalVariation_depth01_EGTB() {
        game = Game.from(FEN.of("4k3/8/8/5p2/6P1/2N5/8/4K3 w - - 0 1"));
        PVCalculatorTriangular.setGame(game);
        PVCalculatorTriangular.setDepth(1);
        PVCalculatorTriangular.beforeSearch();
        PVCalculatorTriangular.beforeSearchByDepth();

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.g4, Square.f5);

        startExecutedMove.executeMove();

        final long nextZobrist = game.getPosition().getZobristHash();

        final short bestMove = 0;
        final int bestValue = Evaluator.WHITE_WON;

        when(evaluator.evaluate()).thenReturn(0);
        when(endGameTableBase.isProbeAvailable()).thenReturn(true);
        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        PVCalculatorTriangular.calculatePrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = PVCalculatorTriangular.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        assertTrue(PVCalculatorTriangular.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(nextZobrist, game.getPosition().getZobristHash());
    }

}
