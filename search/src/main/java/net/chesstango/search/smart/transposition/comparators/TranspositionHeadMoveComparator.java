package net.chesstango.search.smart.transposition.comparators;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.transposition.TTable;
import net.chesstango.search.smart.transposition.TranspositionEntry;
import net.chesstango.search.sorters.MoveComparator;
import net.chesstango.search.sorters.SortListener;

/**
 * @author Mauricio Coria
 */
public class TranspositionHeadMoveComparator implements MoveComparator, Acceptor, SortListener {

    private final TranspositionEntry entryWorkspace;

    @Getter
    @Setter
    private MoveComparator next;

    @Getter
    @Setter
    private TTable tTable;

    @Setter
    private Game game;

    @Setter(AccessLevel.PACKAGE)
    @Getter(AccessLevel.PACKAGE)
    private short bestMoveEncoded;

    public TranspositionHeadMoveComparator() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public void beforeSort(final int currentPly) {
        long hash = game.getPosition().getZobristHash();

        if (tTable.load(hash, entryWorkspace) && hash == entryWorkspace.getHash()) {
            bestMoveEncoded = entryWorkspace.getMove();
        } else {
            bestMoveEncoded = 0;
        }
    }


    @Override
    public int compare(Move o1, Move o2) {
        if (bestMoveEncoded != 0 && o1.binaryEncoding() != o2.binaryEncoding()) {
            if (o1.binaryEncoding() == bestMoveEncoded) {
                return 1;
            } else if (o2.binaryEncoding() == bestMoveEncoded) {
                return -1;
            }
        }

        return next.compare(o1, o2);
    }
}
