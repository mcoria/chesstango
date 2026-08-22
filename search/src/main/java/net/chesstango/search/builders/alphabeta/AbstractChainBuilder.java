package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.AlphaBetaFilter;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.core.filters.AlphaBeta;
import net.chesstango.search.smart.core.filters.QuiescenceStandingPat;
import net.chesstango.search.smart.debug.filters.DebugFilter;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.pv.filters.ExtendPV;
import net.chesstango.search.smart.pv.filters.PropagatePV;
import net.chesstango.search.smart.root.filters.AspirationWindows;
import net.chesstango.search.smart.root.filters.RootMoveEvaluationTracker;
import net.chesstango.search.smart.root.filters.StopProcessingCatch;
import net.chesstango.search.smart.statistics.node.filters.*;
import net.chesstango.search.smart.transposition.filters.*;
import net.chesstango.search.smart.zobrist.filters.ZobristTracker;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public abstract class AbstractChainBuilder {

    protected SearchListenerMediator searchListenerMediator;

    public AlphaBetaFilter build() {
        buildObjects();

        setupListenerMediator();

        return buildAlphaBetaChain();
    }

    public void link() {
    }

    protected abstract void buildObjects();

    protected abstract void setupListenerMediator();

    protected abstract AlphaBetaFilter buildAlphaBetaChain();


    protected AlphaBetaFilter createChain(List<AlphaBetaFilter> chain) {
        for (int i = 0; i < chain.size() - 1; i++) {
            AlphaBetaFilter currentFilter = chain.get(i);
            AlphaBetaFilter next = chain.get(i + 1);

            switch (currentFilter) {
                case TranspositionTableRoot filer -> filer.setNext(next);
                case TranspositionTableTerminal transpositionTableTerminal -> transpositionTableTerminal.setNext(next);
                case TranspositionTable table -> table.setNext(next);
                case TranspositionTableQ transpositionTableQ -> transpositionTableQ.setNext(next);
                case TranspositionTableLeaf transpositionTableLeaf -> transpositionTableLeaf.setNext(next);

                case AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics ->
                        alphaBetaRootNodeStatistics.setNext(next);
                case AlphaBetaInteriorNodeVisited alphaBetaNodeStatistics -> alphaBetaNodeStatistics.setNext(next);
                case AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected ->
                        alphaBetaInteriorNodeExpected.setNext(next);
                case AlphaBetaQuiescenceNodeVisited alphaBetaQuiescenceNodeVisited ->
                        alphaBetaQuiescenceNodeVisited.setNext(next);
                case AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected ->
                        alphaBetaQuiescenceNodeExpected.setNext(next);
                case AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics ->
                        alphaBetaLeafNodeStatistics.setNext(next);
                case AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics ->
                        alphaBetaTerminalNodeStatistics.setNext(next);
                case AlphaBetaLoopNodeStatistics alphaBetaLoopNodeStatistics ->
                        alphaBetaLoopNodeStatistics.setNext(next);
                case AlphaBetaEgtbNodeStatistics alphaBetaEgtbNodeStatistics ->
                        alphaBetaEgtbNodeStatistics.setNext(next);


                case AlphaBeta alphaBeta -> alphaBeta.setNext(next);
                case QuiescenceStandingPat quiescenceStandingPat -> quiescenceStandingPat.setNext(next);

                case DebugFilter debugFilter -> debugFilter.setNext(next);

                case ZobristTracker zobristTracker -> zobristTracker.setNext(next);

                case ExtendPV extendPV -> extendPV.setNext(next);
                case PropagatePV propagatePV -> propagatePV.setNext(next);

                case KillerMoveTracker killerMoveTracker -> killerMoveTracker.setNext(next);

                case StopProcessingCatch stopProcessingCatch -> stopProcessingCatch.setNext(next);

                case AspirationWindows aspirationWindows -> aspirationWindows.setNext(next);

                case RootMoveEvaluationTracker moveEvaluationTracker -> moveEvaluationTracker.setNext(next);

                case null -> throw new RuntimeException(String.format("filter %d is null", i));

                default -> throw new RuntimeException("filter not found: " + currentFilter.getClass().getSimpleName());
            }
        }

        return chain.getFirst();
    }
}
