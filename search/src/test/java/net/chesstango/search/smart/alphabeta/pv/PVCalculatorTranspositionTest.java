package net.chesstango.search.smart.alphabeta.pv;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * @author Mauricio Coria
 */
@ExtendWith(MockitoExtension.class)
public class PVCalculatorTranspositionTest {

    @Mock
    private Evaluator evaluator;

    @Mock
    private EndGameTableBase endGameTableBase;

    @Mock
    private TTable maxMap;

    @Mock
    private TTable minMap;

    @Mock
    private TTable qMaxMap;

    @Mock
    private TTable qMinMap;

    private PVCalculatorTransposition transpositionPVReader;

    private Game game;

    @BeforeEach
    public void setup() {
        transpositionPVReader = new PVCalculatorTransposition();

        transpositionPVReader.setEvaluator(evaluator);
        transpositionPVReader.setEndGameTableBase(endGameTableBase);
        transpositionPVReader.setMaxMap(maxMap);
        transpositionPVReader.setMinMap(minMap);
    }

    @Test
    void test_beforeSearch() {
        game = Game.from(FEN.START_POSITION);
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(1);

        // Supongamos que se ejecutó calculatePrincipalVariation
        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);
        transpositionPVReader.setPrincipalVariation(List.of(new PrincipalVariation(startZobrist, startExecutedMove)));
        transpositionPVReader.setPvComplete(true);

        // Y continuamos con la siguiente busqueda
        transpositionPVReader.beforeSearch();

        assertFalse(transpositionPVReader.isPvComplete());
        assertNull(transpositionPVReader.getPrincipalVariation());
    }

    @Test
    void test_beforeSearchByDepth() {
        game = Game.from(FEN.START_POSITION);
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(2);

        // Supongamos que se ejecutó calculatePrincipalVariation
        final long startZobrist = game.getPosition().getZobristHash();
        final Move startExecutedMove = game.getMove(Square.a2, Square.a4);
        transpositionPVReader.setPrincipalVariation(List.of(new PrincipalVariation(startZobrist, startExecutedMove)));
        transpositionPVReader.setPvComplete(true);

        // Y continuamos con la siguiente profundidad
        transpositionPVReader.beforeSearchByDepth();

        // Entonces pvComplete debe ser false y PV null
        assertFalse(transpositionPVReader.isPvComplete());
        assertNull(transpositionPVReader.getPrincipalVariation());
    }


    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 1
     * PV = {g1f3}
     */
    @Test
    void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(1);
        transpositionPVReader.beforeSearch();
        transpositionPVReader.beforeSearchByDepth();

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        // 0x463B96181691FC9CL
        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        final short secondMovePV = 0;       // Un solo movimiento
        final int bestValue = 10;

        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        transpositionPVReader.calculatePrincipalVariation(secondMovePV, bestValue);

        List<PrincipalVariation> pv = transpositionPVReader.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        assertTrue(transpositionPVReader.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 2
     * PV = {g1f3, d7d5}
     */
    @Test
    void test_calculatePrincipalVariation_depth02() {
        game = Game.from(FEN.START_POSITION);
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(2);
        transpositionPVReader.beforeSearch();
        transpositionPVReader.beforeSearchByDepth();

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();
        final Move secondMove = game.getMove(Square.d7, Square.d5);

        final short secondMovePV = secondMove.binaryEncoding();
        final int bestValue = 10;
        when(evaluator.evaluate()).thenReturn(bestValue);

        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        transpositionPVReader.calculatePrincipalVariation(secondMovePV, bestValue);

        List<PrincipalVariation> pv = transpositionPVReader.getPrincipalVariation();

        assertEquals(2, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        PrincipalVariation lastPV = pv.getLast();
        assertEquals(zobristBeforeCalculate, lastPV.hash());
        assertEquals(secondMove, lastPV.move());

        assertTrue(transpositionPVReader.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    /**
     * Este es el test mas simple de todos.
     * Se busca con depth = 2
     * PV = {g1f3, d7d5}
     */
    @Test
    @Disabled
    void test_calculatePrincipalVariation_depth03() {
        game = Game.from(FEN.START_POSITION);
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(2);
        transpositionPVReader.beforeSearch();
        transpositionPVReader.beforeSearchByDepth();

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();
        final Move secondMove = game.getMove(Square.d7, Square.d5);

        final short secondMovePV = secondMove.binaryEncoding();
        final int bestValue = 10;
        when(evaluator.evaluate()).thenReturn(bestValue);


        // Llegamos a este punto antes de llamar a TranspositionPV.calculatePrincipalVariation()
        transpositionPVReader.calculatePrincipalVariation(secondMovePV, bestValue);

        List<PrincipalVariation> pv = transpositionPVReader.getPrincipalVariation();

        assertEquals(2, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        PrincipalVariation lastPV = pv.getLast();
        assertEquals(zobristBeforeCalculate, lastPV.hash());
        assertEquals(secondMove, lastPV.move());

        assertTrue(transpositionPVReader.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }


    @Test
    void test_calculatePrincipalVariation_depth01_EGTB() {
        game = Game.from(FEN.of("4k3/8/8/5p2/6P1/2N5/8/4K3 w - - 0 1"));
        transpositionPVReader.setGame(game);
        transpositionPVReader.setDepth(1);
        transpositionPVReader.beforeSearch();
        transpositionPVReader.beforeSearchByDepth();

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
        transpositionPVReader.calculatePrincipalVariation(bestMove, bestValue);

        List<PrincipalVariation> pv = transpositionPVReader.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(startZobrist, firstPV.hash());
        assertEquals(startExecutedMove, firstPV.move());

        assertTrue(transpositionPVReader.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(nextZobrist, game.getPosition().getZobristHash());
    }

    /*
    static void main(String[] args) {
        HexFormat hexFormat = HexFormat.of().withUpperCase();

        Game game = Game.from(FEN.START_POSITION);

        long hash = game.getPosition().getZobristHash();
        Move move = game.getMove(Square.g1, Square.f3);
        System.out.printf("g1f3: Hash=0x%sL, Move=0x%s%n", hexFormat.toHexDigits(hash), hexFormat.toHexDigits(move.binaryEncoding()));

        move.executeMove();

        hash = game.getPosition().getZobristHash();
        move = game.getMove(Square.d7, Square.d5);
        System.out.printf("d7d5: Hash=0x%sL, Move=0x%s%n", hexFormat.toHexDigits(hash), hexFormat.toHexDigits(move.binaryEncoding()));

        move.executeMove();

        hash = game.getPosition().getZobristHash();
        move = game.getMove(Square.d2, Square.d4);
        System.out.printf("d2d4: Hash=0x%sL, Move=0x%s%n", hexFormat.toHexDigits(hash), hexFormat.toHexDigits(move.binaryEncoding()));
    }
     */
}
