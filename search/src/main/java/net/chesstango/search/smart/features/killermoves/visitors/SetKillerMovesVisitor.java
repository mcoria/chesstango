package net.chesstango.search.smart.features.killermoves.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.features.killermoves.filters.KillerMoveTracker;

/**
 *
 * @author Mauricio Coria
 */
public class SetKillerMovesVisitor implements Visitor {

    private final KillerMoves killerMoves;

    public SetKillerMovesVisitor(KillerMoves killerMoves) {
        this.killerMoves = killerMoves;
    }

    @Override
    public void visit(KillerMoveTracker killerMoveTracker) {
        killerMoveTracker.setKillerMoves(killerMoves);
    }


    @Override
    public void visit(KillerMoveComparator killerMoveComparator) {
        killerMoveComparator.setKillerMoves(killerMoves);
    }

}
