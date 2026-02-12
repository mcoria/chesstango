package net.chesstango.search.smart.features.pv.filters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.filters.AlphaBetaFilter;
import net.chesstango.search.smart.features.transposition.TTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class TranspositionPVTest {

    @Mock
    private AlphaBetaFilter nextFilter;

    @Mock
    private Evaluator evaluator;

    @Mock
    private TTable maxMap;

    @Mock
    private TTable minMap;

    @Mock
    private TTable qMaxMap;

    @Mock
    private TTable qMinMap;

    private TranspositionPV transpositionPV = new TranspositionPV();

    private Game game;

    @BeforeEach
    public void setup() {
        transpositionPV = new TranspositionPV();
        transpositionPV.setNext(nextFilter);
        transpositionPV.setEvaluator(evaluator);
        transpositionPV.setMaxMap(maxMap);
        transpositionPV.setMinMap(minMap);
        transpositionPV.setQMaxMap(qMaxMap);
        transpositionPV.setQMinMap(qMinMap);
    }

    /**
     * Este es el test mas simple de todos.
     * Se buscó con depth = 1
     * PV = {a2a4}
     */
    @Test
    void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        transpositionPV.setGame(game);
        transpositionPV.setMaxPly(1);

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        startExecutedMove.executeMove();

        final long nextZobrist = game.getPosition().getZobristHash();

        final short bestMove = 0;
        final int bestValue = 10;

        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        transpositionPV.calculatePrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = transpositionPV.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startExecutedMove, firstPV.move());
        assertEquals(startZobrist, firstPV.hash());

        assertTrue(transpositionPV.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(nextZobrist, game.getPosition().getZobristHash());
    }

}
