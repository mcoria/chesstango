package net.chesstango.search.smart.alphabeta.transposition.comparators;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.alphabeta.transposition.TTable;
import net.chesstango.search.smart.alphabeta.transposition.TranspositionEntry;
import net.chesstango.search.smart.sorters.MoveComparator;

/**
 * @author Mauricio Coria
 */
@Setter
public class TranspositionHeadMoveComparator implements MoveComparator, Acceptor {

    @Getter
    private MoveComparator next;

    private Game game;

    @Getter
    private TTable tTable;

    private short bestMoveEncoded;

    private final TranspositionEntry entryWorkspace;

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

        boolean load = tTable.load(hash, entryWorkspace) ;

        if (load) {
            bestMoveEncoded = entryWorkspace.getMove();
        } else {
            bestMoveEncoded = 0;
        }

        next.beforeSort(currentPly);
    }

    @Override
    public void afterSort() {
        next.afterSort();
    }


    @Override
    public int compare(Move o1, Move o2) {
        if (bestMoveEncoded != 0) {
            if (o1.binaryEncoding() == bestMoveEncoded) {
                return 1;
            } else if (o2.binaryEncoding() == bestMoveEncoded) {
                return -1;
            }
        }

        return next.compare(o1, o2);
    }
}
