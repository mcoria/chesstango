package net.chesstango.search.alphabeta.transposition.groupsorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.containers.MoveToHashMap;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.alphabeta.transposition.TTable;
import net.chesstango.search.alphabeta.transposition.TranspositionEntry;

import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class TranspositionTailMoveGroup implements Acceptor, Predicate<Move> {

    private final TranspositionEntry entryWorkspace;

    @Getter
    @Setter
    private TTable tTable;

    @Setter
    private MoveToHashMap moveToZobrist;


    public TranspositionTailMoveGroup() {
        entryWorkspace = new TranspositionEntry();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean test(Move move) {
        long hash = getZobristHashMove(move);
        return tTable.load(hash, entryWorkspace) && hash == entryWorkspace.getHash();
    }

    long getZobristHashMove(Move move) {
        long hash = moveToZobrist.read(move);
        if (hash == 0) {
            hash = move.getZobristHash();
            moveToZobrist.write(move, hash);
        }
        return hash;
    }
}
