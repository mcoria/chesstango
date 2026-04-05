package net.chesstango.search.smart.alphabeta.killermoves.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
public class KillerMoveComparator implements MoveComparator {

    @Getter
    @Setter
    private MoveComparator next;

    private int currentPly;

    @Setter
    private KillerMoves killerMoves;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(int currentPly) {
        this.currentPly = currentPly;
        this.next.beforeSort(currentPly);
    }

    @Override
    public void afterSort(int currentPly) {
        this.next.afterSort(currentPly);
    }

    @Override
    public int compare(Move o1, Move o2) {
        boolean o1IsKiller = killerMoves.isKiller(o1, currentPly);

        boolean o2IsKiller = killerMoves.isKiller(o2, currentPly);

        if (o1IsKiller && !o2IsKiller) {
            return 1;
        } else if (!o1IsKiller && o2IsKiller) {
            return -1;
        }

        return next.compare(o1, o2);
    }
}
