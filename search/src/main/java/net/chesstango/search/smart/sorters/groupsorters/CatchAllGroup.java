package net.chesstango.search.smart.sorters.groupsorters;

import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.sorters.GroupSorter;
import net.chesstango.search.smart.sorters.MoveComparator;
import net.chesstango.search.smart.sorters.comparators.DefaultMoveComparator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mauricio Coria
 */
public class CatchAllGroup implements GroupSorter {
    private final List<Move> collectedMoves;
    private final MoveComparator defaultMoveComparator;

    public CatchAllGroup() {
        this.collectedMoves = new ArrayList<>();
        this.defaultMoveComparator = new DefaultMoveComparator();
    }

    public CatchAllGroup(List<Move> collectedMoves, MoveComparator defaultMoveComparator) {
        this.collectedMoves = collectedMoves;
        this.defaultMoveComparator = defaultMoveComparator;
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
        collectedMoves.sort(defaultMoveComparator.reversed());
        moves.addAll(collectedMoves);
    }

    @Override
    public void beforeSort(int currentPly) {
        collectedMoves.clear();
    }

    @Override
    public void afterSort() {
    }
}
