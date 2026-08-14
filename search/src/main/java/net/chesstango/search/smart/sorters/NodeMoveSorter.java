package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.SearchListenerMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorter implements MoveSorter, Acceptor {
    private final Predicate<Move> filter;

    @Setter
    private MoveToHashMap moveToZobrist;

    @Setter
    private Game game;

    @Getter
    @Setter
    private MoveComparator moveComparator;

    @Setter
    private SearchListenerMediator searchListenerMediator;

    public NodeMoveSorter() {
        this(move -> true);
    }

    public NodeMoveSorter(Predicate<Move> filter) {
        this.filter = filter;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        MoveContainerReader<Move> moves = game.getPossibleMoves();

        List<Move> moveList = new ArrayList<>(moves.size());
        for (Move move : moves) {
            if (filter.test(move)) {
                moveList.add(move);
            }
        }

        moveToZobrist.clear();

        searchListenerMediator.triggerBeforeSort(currentPly);

        moveList.sort(moveComparator.reversed());

        searchListenerMediator.triggerAfterSort();

        return moveList;
    }
}
