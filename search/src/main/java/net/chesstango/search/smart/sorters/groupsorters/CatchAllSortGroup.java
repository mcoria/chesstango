package net.chesstango.search.smart.sorters.groupsorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllSortGroup implements GroupSorter {
    private final List<Move> collectedMoves;

    @Setter
    private MoveComparator moveComparator;

    public CatchAllSortGroup() {
        this.collectedMoves = new ArrayList<>();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean offer(Move move) {
        return collectedMoves.add(move);
    }

    @Override
    public void collect(List<Move> moves) {
        collectedMoves.sort(moveComparator.reversed());
        moves.addAll(collectedMoves);
    }

    @Override
    public void beforeSort(int currentPly) {
        collectedMoves.clear();
        moveComparator.beforeSort(currentPly);
    }

    @Override
    public void afterSort() {
        moveComparator.afterSort();
    }
}
