package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.search.smart.transposition.TranspositionBound;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Mauricio Coria
 */
public class NodeSorter01Test extends AbstractNodeSorterTest {


    @Test
    public void test01() {
        loadTranspositionTables();

        moveSorterBuilder
                .withTranspositionTable()
                .withGameEvaluatorCache(loadEvaluationCache());

        MoveSorter moveSorter = moveSorterBuilder.build();

        smartListenerMediator.triggerBeforeSearch(cycleContext);
        smartListenerMediator.triggerBeforeSearchByDepth(depthContext);

        Iterable<Move> orderedMoves = moveSorter.getOrderedMoves(2);

        List<String> orderedMovesStr = convertMoveListToStringList(orderedMoves);

        assertEquals("[e6h6, e6g4, e6e1, f3d3, e6e2, e6e5, e6d5, e6a2, e6b3, e6f5, e6e4, e6c4, e6e3, e6g6, e6f6, e6d6, e6c6, e6b6, e6a6, e6f7, e6e7, e6d7, e6g8, e6e8, e6c8, f3f1, f3f2, f3h3, f3g3, f3e3, f3f4, f3f5, f3f6, f3f7, h7h5, h7h6, g7g5, g7g6, c7c5, c7c6, h8g8]",
                orderedMovesStr.toString());
    }

    @Override
    protected Game createGame() {
        return FENDecoder.loadGame("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - 0 1")
                .executeMove(Square.f6, Square.f3)
                .executeMove(Square.h3, Square.h4);

    }

    @Override
    protected int getMaxSearchPly() {
        return 3;
    }

    protected void loadTranspositionTables() {
        minMap.write(0xF91593D0EB65C164L, 1, 3147906084927624L, TranspositionBound.UPPER_BOUND); // e6h6
    }

    protected GameEvaluatorCacheReadMock loadEvaluationCache() {
        Map<Long, Integer> cacheEvaluation = new HashMap<>();
        cacheEvaluation.put(0x2F1F32B49441E175L, -180480);
        cacheEvaluation.put(0x9976C1E4920C1531L, -96725);
        cacheEvaluation.put(0xA9370C554A4AB059L, -432325);
        cacheEvaluation.put(0x9FDE36217453B78AL, -105285);
        cacheEvaluation.put(0xC916C33B2FA45612L, -94345);
        cacheEvaluation.put(0x594189B86097D249L, -99690);
        cacheEvaluation.put(0x071B2DBC56B401C6L, -104495);
        cacheEvaluation.put(0x474641DC0C14FB79L, -107530);
        cacheEvaluation.put(0x8F51DDCCAB7A56E2L, -96320);
        cacheEvaluation.put(0x45ACAF1C4E505F0DL, -93450);
        cacheEvaluation.put(0x1E8AE0EB11C1EF3AL, -109320);
        cacheEvaluation.put(0x95A2106CBE16BE2FL, -348845);

        return new GameEvaluatorCacheReadMock().setCache(cacheEvaluation);
    }

}
