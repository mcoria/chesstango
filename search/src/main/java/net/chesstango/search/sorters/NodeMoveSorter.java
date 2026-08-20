package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorter implements MoveSorter, Acceptor {
    private final List<SortListener> sortListeners = new LinkedList<>();

    private final Predicate<Move> filter;

    @Setter
    private MoveToHashMap moveToZobrist;

    @Setter
    private Game game;

    @Getter
    @Setter
    private MoveComparator moveComparator;

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

        triggerBeforeSort(currentPly);

        moveList.sort(moveComparator.reversed());

        triggerAfterSort();

        return moveList;
    }

    public void triggerBeforeSort(int currentPly) {
        sortListeners.forEach(sortListener -> sortListener.beforeSort(currentPly));
    }

    public void triggerAfterSort() {
        sortListeners.forEach(SortListener::afterSort);
    }

    public void addSortListener(SortListener sortListener) {
        sortListeners.add(sortListener);
    }
}
