package net.chesstango.search.smart.sorters;

import net.chesstango.board.Game;
import net.chesstango.board.Square;
import net.chesstango.board.representations.fen.FENDecoder;
import net.chesstango.evaluation.GameEvaluatorCacheRead;
import net.chesstango.search.builders.MoveSorterBuilder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByDepthContext;
import net.chesstango.search.smart.SmartListenerMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mauricio Coria
 */
public class NodeSorter01Test {
    private Game game;
    private SearchByCycleContext cycleContext;

    private GameEvaluatorCacheReadMock gameEvaluatorCacheReadMock;
    private Map<Long, Integer> cacheEvaluation;
    private SmartListenerMediator smartListenerMediator;
    private SearchByDepthContext depthContext;

    @BeforeEach
    public void setup(){
        game = FENDecoder.loadGame("1R3b1k/2p3pp/4qr2/Q7/3p2P1/3P3K/6NP/8 b - - 0 1")
                .executeMove(Square.f6, Square.f3)
                .executeMove(Square.h3, Square.h4);

        cacheEvaluation = new HashMap<>();
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

        gameEvaluatorCacheReadMock = new GameEvaluatorCacheReadMock();
        gameEvaluatorCacheReadMock.setCache(cacheEvaluation);

        smartListenerMediator = new SmartListenerMediator();
        cycleContext = new SearchByCycleContext(game);
        depthContext = new SearchByDepthContext(3);
    }

    @Test
    public void test01() {
        MoveSorterBuilder moveSorterBuilder = new MoveSorterBuilder();
        moveSorterBuilder.withGameEvaluatorCache(gameEvaluatorCacheReadMock);
        moveSorterBuilder.withSmartListenerMediator(smartListenerMediator);

        MoveSorter moveSorter = moveSorterBuilder.build();

        smartListenerMediator.triggerBeforeSearch(cycleContext);
        smartListenerMediator.triggerBeforeSearchByDepth(depthContext);

        moveSorter.getOrderedMoves(3);
    }
}
