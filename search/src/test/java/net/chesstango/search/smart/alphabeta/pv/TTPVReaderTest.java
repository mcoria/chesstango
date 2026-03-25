package net.chesstango.search.smart.alphabeta.pv;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
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
public class TTPVReaderTest {

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

    private TTPVReader ttPvReader;

    private Game game;

    @BeforeEach
    public void setup() {
        ttPvReader = new TTPVReader();

        ttPvReader.beforeSearch();
        ttPvReader.beforeSearchByDepth();
        ttPvReader.setEvaluator(evaluator);
        ttPvReader.setMaxMap(maxMap);
        ttPvReader.setMinMap(minMap);
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {a2a4}
     */
    @Test
    void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        ttPvReader.setGame(game);
        ttPvReader.setDepth(1);

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        startExecutedMove.executeMove();

        final long nextZobrist = game.getPosition().getZobristHash();

        final short bestMove = 0;
        final int bestValue = 10;

        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        ttPvReader.readPrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = ttPvReader.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        assertTrue(ttPvReader.isPvComplete());

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
        ttPvReader.setGame(game);
        ttPvReader.setDepth(2);

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        startExecutedMove.executeMove();
        final long nextZobrist = game.getPosition().getZobristHash();
        final Move nextExecutedMove = game.getMove(Square.g7, Square.g5);


        final short bestMove = nextExecutedMove.binaryEncoding();
        final int bestValue = 10;
        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        ttPvReader.readPrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = ttPvReader.getPrincipalVariation();

        assertEquals(2, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        PrincipalVariation lastPV = pv.getLast();
        assertEquals(nextZobrist, lastPV.hash());
        assertEquals(nextExecutedMove, lastPV.move());

        assertTrue(ttPvReader.isPvComplete());

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

        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);

        // Supongamos que se ejecutó readPrincipalVariation
        ttPvReader.principalVariation = List.of(new PrincipalVariation(startZobrist, startExecutedMove));
        ttPvReader.pvComplete = true;

        // Y continuamos con la siguiente profundidad
        ttPvReader.beforeSearchByDepth();

        // Entonces pvComplete debe ser false y el principal variation debe conservar la ultima PV calcualda
        assertFalse(ttPvReader.isPvComplete());
        assertEquals(List.of(new PrincipalVariation(startZobrist, startExecutedMove)), ttPvReader.getPrincipalVariation());
    }

}
