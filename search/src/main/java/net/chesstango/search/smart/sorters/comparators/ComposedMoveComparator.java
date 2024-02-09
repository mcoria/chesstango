package net.chesstango.search.smart.sorters.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public class ComposedMoveComparator implements MoveComparator {

    @Setter
    @Getter
    private TranspositionHeadMoveComparator transpositionHeadMoveComparator;

    @Setter
    @Getter
    private TranspositionTailMoveComparator transpositionTailMoveComparator;

    @Getter
    private final DefaultMoveComparator defaultMoveComparator;

    public ComposedMoveComparator() {
        defaultMoveComparator = new DefaultMoveComparator();
    }

    @Override
    public int compare(Move o1, Move o2) {

        int result = transpositionHeadMoveComparator.compare(o1, o2);

        if (result == 0) {
            result = transpositionTailMoveComparator.compare(o1, o2);

            if (result == 0) {
                result = defaultMoveComparator.compare(o1, o2);
            }
        }

        return result;
    }
}
