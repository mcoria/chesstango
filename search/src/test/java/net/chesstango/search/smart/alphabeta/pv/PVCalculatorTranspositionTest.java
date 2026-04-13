package net.chesstango.search.smart.alphabeta.pv;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.evaluators.EvaluatorByFEN;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.PrincipalVariation;
import net.chesstango.search.smart.alphabeta.egtb.EndGameTableBase;
import net.chesstango.search.smart.alphabeta.transposition.TTableMap;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
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
public class PVCalculatorTranspositionTest {

    private EvaluatorByFEN evaluator;

    @Mock
    private EndGameTableBase endGameTableBase;

    private TTableMap tTableMap;

    private PVCalculatorTransposition pvCalculator;

    private Game game;

    @BeforeEach
    public void setup() {
        evaluator = new EvaluatorByFEN();
        tTableMap = new TTableMap();

        pvCalculator = new PVCalculatorTransposition();
        pvCalculator.setEvaluator(evaluator);
        pvCalculator.setEndGameTableBase(endGameTableBase);
        pvCalculator.setTTable(tTableMap);
    }

    @Test
    void test_beforeSearch() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);
        pvCalculator.setDepth(1);

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
    void test_calculatePrincipalVariation_depth01() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);
        pvCalculator.setDepth(1);
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkbnr/pppppppp/8/8/8/5N2/PPPPPPPP/RNBQKB1R b KQkq - 1 1", 10);

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        // 0x463B96181691FC9CL
        final long zobristBeforeCalculate = game.getPosition().getZobristHash();

        // Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
        pvCalculator.calculatePrincipalVariation(10);

        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

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
    void test_calculatePrincipalVariation_depth02() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);
        pvCalculator.setDepth(2);
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkbnr/ppp1pppp/8/3p4/8/5N2/PPPPPPPP/RNBQKB1R w KQkq d6 0 2", 10);

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();
        writeTT(tTableMap, zobristBeforeCalculate, (short) 0x0CE3, 10);

        // Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
        pvCalculator.calculatePrincipalVariation(10);

        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(2, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        PrincipalVariation lastPV = pv.getLast();
        assertEquals(zobristBeforeCalculate, lastPV.hash());
        assertEquals((short) 0x0CE3, lastPV.move().binaryEncoding());

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
    void test_calculatePrincipalVariation_depth03() {
        game = Game.from(FEN.START_POSITION);
        pvCalculator.setGame(game);
        pvCalculator.setDepth(3);
        pvCalculator.beforeSearch();

        evaluator.setGame(game);
        evaluator.addEvaluation("rnbqkbnr/ppp1pppp/8/3p4/3P4/5N2/PPP1PPPP/RNBQKB1R b KQkq d3 0 2", 10);

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g1, Square.f3);
        firstMove.executeMove();

        final long zobristBeforeCalculate = game.getPosition().getZobristHash();
        writeTT(tTableMap, zobristBeforeCalculate, (short) 0x0CE3, 10);
        writeTT(tTableMap, 0x183558FAE2A3D387L, (short) 0x02DB, 10);

        // Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
        pvCalculator.calculatePrincipalVariation(10);

        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(3, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        PrincipalVariation secondPV = pv.get(1);
        assertEquals(zobristBeforeCalculate, secondPV.hash());
        assertEquals((short) 0x0CE3, secondPV.move().binaryEncoding());

        PrincipalVariation thirdPV = pv.get(2);
        assertEquals(0x183558FAE2A3D387L, thirdPV.hash());
        assertEquals((short) 0x02DB, thirdPV.move().binaryEncoding());

        assertTrue(pvCalculator.isPvComplete());

        // Verifica que el undo fué correcto
        assertEquals(zobristBeforeCalculate, game.getPosition().getZobristHash());
    }

    @Test
    void test_calculatePrincipalVariation_depth01_EGTB() {
        game = Game.from(FEN.of("4k3/8/8/5p2/6P1/2N5/8/4K3 w - - 0 1"));
        pvCalculator.setGame(game);
        pvCalculator.setDepth(1);
        pvCalculator.beforeSearch();

        evaluator.setGame(game);

        final long firstZobrist = game.getPosition().getZobristHash();
        final Move firstMove = game.getMove(Square.g4, Square.f5);
        firstMove.executeMove();

        final long nextZobrist = game.getPosition().getZobristHash();

        when(endGameTableBase.isProbeAvailable()).thenReturn(true);
        when(endGameTableBase.evaluate()).thenReturn(Evaluator.WHITE_WON);

        // Llegamos a este punto antes de llamar a TranspositionPV.walkPrincipalVariation()
        pvCalculator.calculatePrincipalVariation(Evaluator.WHITE_WON);

        List<PrincipalVariation> pv = pvCalculator.getPrincipalVariation();

        assertEquals(1, pv.size());

        PrincipalVariation firstPV = pv.getFirst();
        assertEquals(firstZobrist, firstPV.hash());
        assertEquals(firstMove, firstPV.move());

        assertTrue(pvCalculator.isPvComplete());

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


    private void writeTT(TTableMap tTable, long hash, short move, int value) {
        TranspositionEntry entry = new TranspositionEntry()
                .setHash(hash)
                .setBound(Bound.EXACT)
                .setDraft((byte) 0)
                .setMove(move)
                .setValue(value);
        tTable.save(entry);
    }
}
