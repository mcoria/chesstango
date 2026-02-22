package net.chesstango.search.smart.features.statistics.node.listeners;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.GameListener;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.SearchListenerMediator;
import net.chesstango.search.smart.features.statistics.node.NodeStatistics;
import net.chesstango.search.smart.features.statistics.node.visitors.SetStaticCountersVisitor;

/**
 * @author Mauricio Coria
 */
public class SetNodeStatistics implements SearchByCycleListener, Acceptor {

    @Setter
    private Game game;

    @Getter
    private long executedMoves;

    private long[] visitedNodesCounters;
    private long[] expectedNodesCounters;
    private long[] visitedNodesCountersQuiescence;
    private long[] expectedNodesCountersQuiescence;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSearch() {
        this.executedMoves = 0;
        this.visitedNodesCounters = new long[30];
        this.expectedNodesCounters = new long[30];
        this.visitedNodesCountersQuiescence = new long[30];
        this.expectedNodesCountersQuiescence = new long[30];

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


    public NodeStatistics getRegularNodeStatistics() {
        return new NodeStatistics(expectedNodesCounters, visitedNodesCounters);
    }

    public NodeStatistics getQuiescenceNodeStatistics() {
        return new NodeStatistics(expectedNodesCountersQuiescence, visitedNodesCountersQuiescence);
    }
}
