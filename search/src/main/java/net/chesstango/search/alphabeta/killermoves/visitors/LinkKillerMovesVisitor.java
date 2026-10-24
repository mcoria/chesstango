package net.chesstango.search.alphabeta.killermoves.visitors;

import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.alphabeta.killermoves.filters.KillerMoveTracker;
import net.chesstango.search.alphabeta.killermoves.sorters.KillerMovePredicate;
import net.chesstango.search.alphabeta.killermoves.sorters.comparators.KillerMoveComparator;

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

    @Override
    public void visit(KillerMovePredicate killerMovePredicate) {
        killerMovePredicate.setKillerMoves(killerMoves);
    }

}
