package net.chesstango.search.builders.alphabeta;

import net.chesstango.search.smart.alphabeta.AlphaBetaFilter;
import net.chesstango.search.smart.alphabeta.core.filters.AlphaBeta;
import net.chesstango.search.smart.alphabeta.core.filters.once.AspirationWindows;
import net.chesstango.search.smart.alphabeta.core.filters.once.MoveEvaluationTracker;
import net.chesstango.search.smart.alphabeta.core.filters.once.StopProcessingCatch;
import net.chesstango.search.smart.alphabeta.debug.filters.DebugFilter;
import net.chesstango.search.smart.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.smart.alphabeta.pv.filters.TranspositionPV;
import net.chesstango.search.smart.alphabeta.pv.filters.TriangularPV;
import net.chesstango.search.smart.alphabeta.quiescence.Quiescence;
import net.chesstango.search.smart.alphabeta.statistics.node.filters.*;
import net.chesstango.search.smart.alphabeta.transposition.filters.*;
import net.chesstango.search.smart.alphabeta.zobrist.filters.ZobristTracker;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AbstractChainBuilder {

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

                case AlphaBetaRootNodeStatistics alphaBetaRootNodeStatistics -> alphaBetaRootNodeStatistics.setNext(next);
                case AlphaBetaInteriorNodeVisited alphaBetaNodeStatistics -> alphaBetaNodeStatistics.setNext(next);
                case AlphaBetaInteriorNodeExpected alphaBetaInteriorNodeExpected -> alphaBetaInteriorNodeExpected.setNext(next);
                case AlphaBetaQuiescenceNodeVisited alphaBetaQuiescenceNodeVisited -> alphaBetaQuiescenceNodeVisited.setNext(next);
                case AlphaBetaQuiescenceNodeExpected alphaBetaQuiescenceNodeExpected -> alphaBetaQuiescenceNodeExpected.setNext(next);
                case AlphaBetaLeafNodeStatistics alphaBetaLeafNodeStatistics -> alphaBetaLeafNodeStatistics.setNext(next);
                case AlphaBetaTerminalNodeStatistics alphaBetaTerminalNodeStatistics -> alphaBetaTerminalNodeStatistics.setNext(next);
                case AlphaBetaLoopNodeStatistics alphaBetaLoopNodeStatistics -> alphaBetaLoopNodeStatistics.setNext(next);
                case AlphaBetaEgtbNodeStatistics alphaBetaEgtbNodeStatistics -> alphaBetaEgtbNodeStatistics.setNext(next);


                case AlphaBeta alphaBeta -> alphaBeta.setNext(next);
                case Quiescence quiescence -> quiescence.setNext(next);

                case DebugFilter debugFilter -> debugFilter.setNext(next);

                case ZobristTracker zobristTracker -> zobristTracker.setNext(next);

                case TriangularPV triangularPV -> triangularPV.setNext(next);

                case KillerMoveTracker killerMoveTracker -> killerMoveTracker.setNext(next);

                case StopProcessingCatch stopProcessingCatch -> stopProcessingCatch.setNext(next);

                case AspirationWindows aspirationWindows -> aspirationWindows.setNext(next);

                case MoveEvaluationTracker moveEvaluationTracker -> moveEvaluationTracker.setNext(next);

                case TranspositionPV transpositionPV -> transpositionPV.setNext(next);

                case null -> throw new RuntimeException(String.format("filter %d is null", i));

                default -> throw new RuntimeException("filter not found: " + currentFilter.getClass().getSimpleName());
            }
        }

        return chain.getFirst();
    }
}
