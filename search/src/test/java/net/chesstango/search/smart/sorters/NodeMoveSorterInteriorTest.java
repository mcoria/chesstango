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
                .executeMove(Square.e5, Square.f6)
                .executeMove(Square.d7, Square.e7);

        searchListenerMediator.accept(new SetGameVisitor(game));

        ttWrite(0xE0BC0BF54B3421D6L, (byte) 3, (short) 2941, 2147483646, LOWER_BOUND); // f6f8
        ttWrite(0xC11925991E06E7D2L, (byte) 1, (short) 513, 1, LOWER_BOUND); // g1f1
        ttWrite(0xD117385FABD28399L, (byte) 1, (short) 513, 1, LOWER_BOUND); // g1h1
        ttWrite(0xF47BA8D36A2CAE38L, (byte) 1, (short) 513, 0, LOWER_BOUND); // g1h2
        ttWrite(0xCC5AB0B4ABE3F9EEL, (byte) 1, (short) 513, 1, LOWER_BOUND); // g2g3
        ttWrite(0x43F10A23A341CCCBL, (byte) 1, (short) 513, 1, LOWER_BOUND); // g2g4
        ttWrite(0x8BCDB6635D487627L, (byte) 1, (short) 513, 1, LOWER_BOUND); // f3f4
        ttWrite(0xD0533C7C86338047L, (byte) 1, (short) 3836, 0, LOWER_BOUND); // c6d6
        ttWrite(0x16BB42DF297DFB1BL, (byte) 1, (short) 556, 1, LOWER_BOUND); // c6e6
        ttWrite(0x6E4F0269C5E84D61L, (byte) 2, (short) 3113, 0, LOWER_BOUND); // c6b6
        ttWrite(0x75C03376D3D767D3L, (byte) 1, (short) 3826, 1, LOWER_BOUND); // c6c7
        ttWrite(0xFA5D36E13FA17817L, (byte) 1, (short) 3834, 1, LOWER_BOUND); // c6c8
        ttWrite(0x1C2978731AA34BB9L, (byte) 1, (short) 2658, 1, LOWER_BOUND); // c6c5
        ttWrite(0x0A7256AE65CE68FBL, (byte) 1, (short) 538, 1, LOWER_BOUND); // c6c4
        ttWrite(0x5E911DE992D2E936L, (byte) 1, (short) 513, 1, LOWER_BOUND); // c6c3
        ttWrite(0xA444B0346CDB4FCDL, (byte) 1, (short) 522, 1, LOWER_BOUND); // c6c2
        ttWrite(0x659399E2EE1927AAL, (byte) 1, (short) 524, 0, LOWER_BOUND); // c6c1
        ttWrite(0x151D097F68ACB206L, (byte) 1, (short) 3382, 9, LOWER_BOUND); // f6g7
        ttWrite(0x97B67985BCD3C91EL, (byte) 2, (short) 3388, 0, LOWER_BOUND); // f6h8
        ttWrite(0xFC0EF73D55AD5D08L, (byte) 1, (short) 3046, 9, LOWER_BOUND); // f6g5
        ttWrite(0x5E47A6ABACE48B1AL, (byte) 1, (short) 513, 1, LOWER_BOUND); // f6h4
        ttWrite(0x4915CCBCB9C24C5FL, (byte) 1, (short) 3379, 0, LOWER_BOUND); // f6e5
        ttWrite(0xB64D137892202CCEL, (byte) 1, (short) 3379, 0, LOWER_BOUND); // f6d4
        ttWrite(0x7525099F1A8F720DL, (byte) 1, (short) 513, 1, LOWER_BOUND); // f6c3
        ttWrite(0x663D1A745923E2EDL, (byte) 1, (short) 521, 9, LOWER_BOUND); // f6b2
        ttWrite(0x4DFC0D7018778CCBL, (byte) 1, (short) 512, 10, LOWER_BOUND); // f6a1
        ttWrite(0xAF05FD4F362A800CL, (byte) 1, (short) 3828, 4, LOWER_BOUND); // f6e7
        ttWrite(0x1384227562340005L, (byte) 1, (short) 512, 1, LOWER_BOUND); // f6g6
        ttWrite(0xB90B794FA54439A9L, (byte) 2, (short) 513, 0, LOWER_BOUND); // f6h6
        ttWrite(0x10A970436A73F0ABL, (byte) 1, (short) 3372, 5, LOWER_BOUND); // f6e6
        ttWrite(0xCC958ACBF3736295L, (byte) 1, (short) 3379, 0, LOWER_BOUND); // f6d6
        ttWrite(0x1752BCAA1CC2E3E4L, (byte) 1, (short) 3381, 9, LOWER_BOUND); // f6f7
        ttWrite(0xF3E2B24A58AD3211L, (byte) 1, (short) 512, 1, LOWER_BOUND); // f6f5
        ttWrite(0x48D2395C33B5D27CL, (byte) 1, (short) 513, 1, LOWER_BOUND); // f6f4

        cacheEvaluationWrite(0xC11925991E06E7D2L, -1); // g1f1
        cacheEvaluationWrite(0xD117385FABD28399L, -1); // g1h1
        cacheEvaluationWrite(0xCC5AB0B4ABE3F9EEL, -1); // g2g3
        cacheEvaluationWrite(0x43F10A23A341CCCBL, -1); // g2g4
        cacheEvaluationWrite(0x8BCDB6635D487627L, -1); // f3f4
        cacheEvaluationWrite(0xF47BA8D36A2CAE38L, -1); // g1h2
        cacheEvaluationWrite(0xD0533C7C86338047L, -1); // c6d6
        cacheEvaluationWrite(0x16BB42DF297DFB1BL, -1); // c6e6
        cacheEvaluationWrite(0x75C03376D3D767D3L, -1); // c6c7
        cacheEvaluationWrite(0x1C2978731AA34BB9L, -1); // c6c5
        cacheEvaluationWrite(0x0A7256AE65CE68FBL, -1); // c6c4
        cacheEvaluationWrite(0x5E911DE992D2E936L, -1); // c6c3
        cacheEvaluationWrite(0xA444B0346CDB4FCDL, -1); // c6c2
        cacheEvaluationWrite(0x659399E2EE1927AAL, -1); // c6c1
        cacheEvaluationWrite(0x97B67985BCD3C91EL, -1); // f6h8
        cacheEvaluationWrite(0x4915CCBCB9C24C5FL, -1); // f6e5
        cacheEvaluationWrite(0xB64D137892202CCEL, -1); // f6d4
        cacheEvaluationWrite(0x5E47A6ABACE48B1AL, -1); // f6h4
        cacheEvaluationWrite(0x7525099F1A8F720DL, -1); // f6c3
        cacheEvaluationWrite(0xFC0EF73D55AD5D08L, -1); // f6g5
        cacheEvaluationWrite(0x663D1A745923E2EDL, -1); // f6b2
        cacheEvaluationWrite(0x1384227562340005L, -1); // f6g6
        cacheEvaluationWrite(0xCC958ACBF3736295L, -1); // f6d6
        cacheEvaluationWrite(0x1752BCAA1CC2E3E4L, -1); // f6f7
        cacheEvaluationWrite(0xF3E2B24A58AD3211L, -1); // f6f5
        cacheEvaluationWrite(0x48D2395C33B5D27CL, -1); // f6f4
        cacheEvaluationWrite(0x151D097F68ACB206L, -1); // f6g7

        killerMoves.trackKillerMove(getMove(game, "c6c8"), 3); // c6c8;

        List<String> actualSort = toMoveStrList(moveSorterInterior.getOrderedMoves(2));
        assertEquals(List.of("f6f8", "c6b6", "f6h6", "f6h8", "f6d6", "f6e5", "f6d4", "c6d6", "c6c1", "g1h2", "c6c8", "f6g6", "f6f5", "f6h4", "f6f4", "f6c3", "c6c7", "c6e6", "c6c5", "c6c4", "c6c3", "c6c2", "f3f4", "g2g4", "g2g3", "g1h1", "g1f1", "f6e7", "f6e6", "f6g7", "f6f7", "f6g5", "f6b2", "f6a1"), actualSort);
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
