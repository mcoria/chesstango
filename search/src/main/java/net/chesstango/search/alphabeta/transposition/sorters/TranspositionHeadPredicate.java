package net.chesstango.search.alphabeta.transposition.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.sorters.MoveSorterListener;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadPredicate implements Acceptor, Predicate<Move>, MoveSorterListener {

    private final TranspositionEntry entryWorkspace;

    @Getter
    @Setter
    private TTable tTable;

    @Setter
    private Game game;

    @Setter
    @Getter
    private short bestMoveEncoded;

    public TranspositionHeadPredicate() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(int currentPly) {
        long hash = game.getPosition().getZobristHash();

        if (tTable.load(hash, entryWorkspace) && hash == entryWorkspace.getHash()) {
            bestMoveEncoded = entryWorkspace.getMove();
        } else {
            bestMoveEncoded = 0;
        }
    }

    @Override
    public boolean test(Move move) {
        return move.binaryEncoding() == bestMoveEncoded;
    }
}
