package net.chesstango.search.smart.killermoves.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.smart.killermoves.KillerMoves;
import net.chesstango.search.smart.killermoves.comparators.KillerMoveComparator;
import net.chesstango.search.smart.killermoves.filters.KillerMoveTracker;

/**
 *
 * @author Mauricio Coria
 */
public class LinkKillerMovesVisitor implements Visitor {

    private final KillerMoves killerMoves;

    public LinkKillerMovesVisitor(KillerMoves killerMoves) {
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
