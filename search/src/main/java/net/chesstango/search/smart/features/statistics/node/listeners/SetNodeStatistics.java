package net.chesstango.search.smart.features.statistics.node.listeners;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.GameListener;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.SearchResult;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import net.chesstango.search.visitors.SetStaticCountersVisitor;

/**
 * @author Mauricio Coria
 */
public class SetNodeStatistics implements SearchByCycleListener, Acceptor {

    @Setter
    private Game game;
    private int executedMoves;
    private int[] visitedNodesCounters;
    private int[] expectedNodesCounters;
    private int[] visitedNodesCountersQuiescence;
    private int[] expectedNodesCountersQuiescence;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        this.executedMoves = 0;
        this.visitedNodesCounters = new int[30];
        this.expectedNodesCounters = new int[30];
        this.visitedNodesCountersQuiescence = new int[30];
        this.expectedNodesCountersQuiescence = new int[30];

        searchListenerMediator.accept(
                new SetStaticCountersVisitor(
                        visitedNodesCounters, expectedNodesCounters,
                        visitedNodesCountersQuiescence, expectedNodesCountersQuiescence
                )
        );


        game.addGameListener(new GameListener() {
            @Override
            public void notifyDoMove(Move move) {
                executedMoves++;
            }

            @Override
            public void notifyUndoMove(Move move) {
            }
        });
    }

    @Override
    public void afterSearch(SearchResult result) {
        if (result != null) {
            result.setExecutedMoves(executedMoves);
            result.setRegularNodeStatistics(new NodeStatistics(expectedNodesCounters, visitedNodesCounters));
            result.setQuiescenceNodeStatistics(new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence));
        }
    }
}
