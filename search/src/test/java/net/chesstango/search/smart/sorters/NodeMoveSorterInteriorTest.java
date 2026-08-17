package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.evaluation.Evaluator;
import net.chesstango.evaluation.EvaluatorCache;
import net.chesstango.gardel.fen.FEN;
import net.chesstango.search.Bound;
import net.chesstango.search.builders.EvaluationBuilder;
import net.chesstango.search.builders.KillerMoveBuilder;
import net.chesstango.search.builders.TranspositionTableBuilder;
import net.chesstango.search.builders.sorters.MoveSorterBuilder;
import net.chesstango.search.builders.sorters.MoveSorterInteriorBuilder;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.visitors.LinkMoveToHashMap;
import net.chesstango.search.visitors.SetGameVisitor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static net.chesstango.evaluation.EvaluatorCache.ARRAY_SIZE;
import static net.chesstango.search.Bound.EXACT;
import static net.chesstango.search.Bound.LOWER_BOUND;
import static net.chesstango.search.smart.Constants.DEFAULT_STALE_AGE;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorterInteriorTest {
    private final SimpleMoveEncoder simpleMoveEncoder = SimpleMoveEncoder.INSTANCE;

    private MoveSorter moveSorterInterior;

    private SearchListenerMediator searchListenerMediator;

    private TTable tTable;
    private TranspositionEntry transpositionEntry;

    private KillerMoves killerMoves;

    private EvaluatorCache.GameEvaluatorCacheEntry[] gameEvaluatorCacheEntries;
    private int cacheAge;

    @BeforeEach
    public void setUp() {
        searchListenerMediator = new SearchListenerMediator();

        MoveSorterBuilder moveSorterBuilder = new MoveSorterInteriorBuilder()
                .withIterativeDeepening()
                .withGameEvaluatorCache()
                .withTranspositionTable()
                .withKillerMove()
                .withRecapture()
                .withMvvLva()
                .withSmartListenerMediator(searchListenerMediator);

        TranspositionTableBuilder transpositionTableBuilder = new TranspositionTableBuilder()
                .withHashSize(16)
                .withStaleAge(DEFAULT_STALE_AGE)
                .withSmartListenerMediator(searchListenerMediator);

        KillerMoveBuilder killerMoveBuilder = new KillerMoveBuilder()
                .withSmartListenerMediator(searchListenerMediator);

        EvaluationBuilder evaluationBuilder = new EvaluationBuilder()
                .withGameEvaluator(Evaluator.createInstance())
                .withGameEvaluatorCache()
                .withSmartListenerMediator(searchListenerMediator);

        moveSorterInterior = moveSorterBuilder.build();
        transpositionTableBuilder.build();
        killerMoveBuilder.build();
        evaluationBuilder.build();

        transpositionTableBuilder.link();
        killerMoveBuilder.link();
        evaluationBuilder.link();

        searchListenerMediator.accept(new LinkMoveToHashMap(new MoveToHashMap()));

        tTable = transpositionTableBuilder.getTTableImp();
        transpositionEntry = new TranspositionEntry();

        killerMoves = killerMoveBuilder.getKillerMovesTableImp();

        EvaluatorCache evaluatorCache = evaluationBuilder.getEvaluatorCache();

        gameEvaluatorCacheEntries = evaluatorCache.getCache();
        cacheAge = evaluatorCache.getCurrentAge();
    }


    @Test
    public void test01() {
        Game game = Game.from(FEN.from("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - 0 1"))
                .executeMove(Square.e5, Square.g3);

        searchListenerMediator.accept(new SetGameVisitor(game));

        /**
         * Settup
         */
        ttWrite(0x13A63803694AEEE9L, (byte) 2, (short) 513, 1, LOWER_BOUND); // a2b1
        ttWrite(0x0AC497FAE0E135CEL, (byte) 0, (short) 1422, 7, EXACT);      // a2g2

        killerMoves.trackKillerMove(getMove(game, "a2a1"), 2);
        killerMoves.trackKillerMove(getMove(game, "a2d2"), 2);

        /**
         * Execute
         */

        List<String> actualSort = toMoveStrList(moveSorterInterior.getOrderedMoves(1));

        /**
         * Assertions
         */
        assertEquals(List.of("a2b1", "a2g2", "a2a1", "a2d2", "a2f2", "a2e2", "a2c2", "a2b2", "a2b3", "a2a3", "a2c4", "a2a4", "a2d5", "a2a5", "a2e6", "a2a6", "a2f7", "a2g8", "d7d1", "d7d2", "d7d3", "d7d4", "d7d5", "d7d6", "d7h7", "d7g7", "d7f7", "d7e7", "d7c7", "d7b7", "h6h5", "b6b5", "a7a5", "a7a6", "d8e7", "d8e8"), actualSort);
    }


    @Test
    public void test02() {
        Game game = Game.from(FEN.from("3k4/p2r4/1pR4p/4Q3/8/5P2/q5P1/6K1 w - - 0 1"))
                .executeMove(Square.e5,Square.f6)
                .executeMove(Square.d8,Square.e8);

        searchListenerMediator.accept(new SetGameVisitor(game));

        ttWrite(0xF672025378084819L, (byte) 3, (short) 2746, 2147483646, LOWER_BOUND); // c6c8
        ttWrite(0xD7D72C3F2D3A8E1DL, (byte) 2, (short) 3316, -124724, LOWER_BOUND); // g1f1
        ttWrite(0xC7D931F998EEEA56L, (byte) 2, (short) 513, 3443, LOWER_BOUND); // g1h1
        ttWrite(0xE2B5A1755910C7F7L, (byte) 2, (short) 513, -81786, LOWER_BOUND); // g1h2
        ttWrite(0x9D03BFC56E741FE8L, (byte) 2, (short) 513, -4306, LOWER_BOUND); // f3f4
        ttWrite(0xC69D35DAB50FE988L, (byte) 2, (short) 3307, -8295, LOWER_BOUND); // c6d6
        ttWrite(0x00754B791A4192D4L, (byte) 2, (short) 556, -350216, EXACT); // c6e6
        ttWrite(0x78810BCFF6D424AEL, (byte) 2, (short) 3113, 301454, LOWER_BOUND); // c6b6
        ttWrite(0x630E3AD0E0EB0E1CL, (byte) 2, (short) 3314, 382022, LOWER_BOUND); // c6c7
        ttWrite(0x0AE771D5299F2276L, (byte) 2, (short) 2658, 377260, LOWER_BOUND); // c6c5
        ttWrite(0x1CBC5F0856F20134L, (byte) 2, (short) 538, 430534, LOWER_BOUND); // c6c4
        ttWrite(0x485F144FA1EE80F9L, (byte) 2, (short) 513, 17021, LOWER_BOUND); // c6c3
        ttWrite(0xB28AB9925FE72602L, (byte) 2, (short) 522, 441911, LOWER_BOUND); // c6c2
        ttWrite(0x735D9044DD254E65L, (byte) 2, (short) 3316, -82414, LOWER_BOUND); // c6c1
        ttWrite(0x03D300D95B90DBC9L, (byte) 2, (short) 3318, 732946, LOWER_BOUND); // f6g7
        ttWrite(0xEAC0FE9B669134C7L, (byte) 2, (short) 3046, 762667, LOWER_BOUND); // f6g5
        ttWrite(0x4889AF0D9FD8E2D5L, (byte) 2, (short) 3893, -72234, LOWER_BOUND); // f6h4
        ttWrite(0xA0831ADEA11C4501L, (byte) 2, (short) 3899, -95776, LOWER_BOUND); // f6d4
        ttWrite(0x63EB003929B31BC2L, (byte) 2, (short) 3893, -61786, LOWER_BOUND); // f6c3
        ttWrite(0x70F313D26A1F8B22L, (byte) 2, (short) 521, 776726, LOWER_BOUND); // f6b2
        ttWrite(0x5B3204D62B4BE504L, (byte) 2, (short) 512, 825514, LOWER_BOUND); // f6a1
        ttWrite(0xCF864A477AB21A65L, (byte) 2, (short) 3316, 736499, LOWER_BOUND); // f6e7
        ttWrite(0x3056087F363204D7L, (byte) 2, (short) 3323, 750081, LOWER_BOUND); // f6d8
        ttWrite(0xAFC570E996785066L, (byte) 2, (short) 513, -54226, LOWER_BOUND); // f6h6
        ttWrite(0x066779E5594F9964L, (byte) 2, (short) 556, 56891, LOWER_BOUND); // f6e6
        ttWrite(0xDA5B836DC04F0B5AL, (byte) 2, (short) 3316, -95198, LOWER_BOUND); // f6d6
        ttWrite(0x019CB50C2FFE8A2BL, (byte) 2, (short) 3317, 740279, LOWER_BOUND); // f6f7
        ttWrite(0x3C12FD2247BF402CL, (byte) 2, (short) 3901, 747364, LOWER_BOUND); // f6f8
        ttWrite(0xE52CBBEC6B915BDEL, (byte) 2, (short) 3892, -90510, LOWER_BOUND); // f6f5
        ttWrite(0x5E1C30FA0089BBB3L, (byte) 2, (short) 3892, -95776, LOWER_BOUND); // f6f4

        cacheEvaluationWrite(0xDA94B91298DF9021L, 36919); // g2g3
        cacheEvaluationWrite(0x553F0385907DA504L, 31216); // g2g4
        cacheEvaluationWrite(0x817870238FEFA0D1L, 23653); // f6h8
        cacheEvaluationWrite(0x5FDBC51A8AFE2590L, 38434); // f6e5
        cacheEvaluationWrite(0x054A2BD3510869CAL, 23560); // f6g6
        cacheEvaluationWrite(0xA0831ADEA11C4501L, 25759); // f6d4
        cacheEvaluationWrite(0x5E1C30FA0089BBB3L, 16450); // f6f4

        List<String> actualSort = toMoveStrList(moveSorterInterior.getOrderedMoves(2));
        assertEquals(List.of("c6c8", "c6e6", "g1f1", "f6d4", "f6f4", "f6d6", "f6f5", "c6c1", "g1h2", "f6h4", "f6c3", "f6h6", "c6d6", "f3f4", "g1h1", "c6c3", "f6e6", "c6b6", "c6c5", "c6c7", "c6c4", "c6c2", "f6g7", "f6e7", "f6f7", "f6f8", "f6d8", "f6g5", "f6b2", "f6a1", "f6e5", "g2g3", "g2g4", "f6h8", "f6g6"), actualSort);

    }

    private void cacheEvaluationWrite(long hash, int value) {
        int idx = (int) Math.abs(hash % ARRAY_SIZE);
        EvaluatorCache.GameEvaluatorCacheEntry entry = gameEvaluatorCacheEntries[idx];
        entry.setHash(hash);
        entry.setEvaluation(value);
        entry.setAge(cacheAge);
    }

    private void ttWrite(long hash, byte draft, short move, int value, Bound bound) {
        transpositionEntry.setHash(hash);
        transpositionEntry.setDraft(draft);
        transpositionEntry.setMove(move);
        transpositionEntry.setValue(value);
        transpositionEntry.setBound(bound);
        tTable.save(transpositionEntry);
    }

    private Move getMove(Game game, String moveStr) {
        for (Move move : game.getPossibleMoves()) {
            if (moveStr.equals(simpleMoveEncoder.encode(move))) {
                return move;
            }
        }
        throw new RuntimeException("No se encontro el movimiento " + moveStr);
    }

    private List<String> toMoveStrList(Iterable<Move> orderedMoves) {
        List<String> result = new ArrayList<>();
        for (Move move : orderedMoves) {
            result.add(simpleMoveEncoder.encode(move));
        }
        return result;
    }

}
