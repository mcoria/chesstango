package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveContainerReader;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class NodeGroupSorter implements MoveSorter, Acceptor {
    private final List<SortListener> sortListeners = new LinkedList<>();

    @Setter
    private Game game;

    @Getter
    @Setter
    private GroupSorter groupSorter;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        MoveContainerReader<Move> moves = game.getPossibleMoves();

        List<Move> moveList = new ArrayList<>(moves.size());

        triggerBeforeSort(currentPly);

        for (Move move : moves) {
            if (!groupSorter.offer(move)) {
                throw new RuntimeException("GroupSorter offer returned false");
            }
        }

        groupSorter.collect(moveList);

        triggerAfterSort();

        return moveList;
    }

    public void triggerBeforeSort(int currentPly) {
        sortListeners.forEach(sortListener -> sortListener.beforeSort(currentPly));
    }

    public void triggerAfterSort() {
        sortListeners.forEach(SortListener::afterSort);
    }
}
