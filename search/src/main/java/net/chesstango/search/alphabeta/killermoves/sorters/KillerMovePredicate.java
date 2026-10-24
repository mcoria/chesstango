package net.chesstango.search.alphabeta.killermoves.sorters;

import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.killermoves.KillerMoves;
import net.chesstango.search.sorters.MoveSorterListener;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class KillerMovePredicate implements Predicate<Move>, Acceptor, MoveSorterListener {

    @Setter
    private KillerMoves killerMoves;

    private int currentPly;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(int currentPly) {
        this.currentPly = currentPly;
    }

    @Override
    public boolean test(Move move) {
        return killerMoves.isKiller(move, currentPly);
    }
}
