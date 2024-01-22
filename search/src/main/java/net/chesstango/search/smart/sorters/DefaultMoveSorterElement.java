package net.chesstango.search.smart.sorters;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class DefaultMoveSorterElement implements MoveSorterElement {

    private static final MoveComparator moveComparator = new MoveComparator();

    @Override
    public void sort(List<Move> unsortedMoves, List<Move> sortedMoves) {
        unsortedMoves.sort(moveComparator.reversed());

        sortedMoves.addAll(unsortedMoves);

        unsortedMoves.clear();
    }
}
