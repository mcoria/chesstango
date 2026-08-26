package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugReadTT;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TTableComparatorTailDebug implements TTable, Acceptor {

    @Setter
    @Getter
    private TTable tTable;

    @Setter
    private Game game;

    @Setter
    private DebugNodeTracker debugNodeTracker;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public boolean load(long hash, TranspositionEntry entry) {
        boolean load = tTable.load(hash, entry);
        if (load) {
            trackReadTranspositionEntry(hash, entry);
        }
        return load;
    }

    @Override
    public void save(TranspositionEntry entry) {
        throw new RuntimeException("Save shold not be called on TTableComparatorDebug");
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugReadTT> readList = currentNode.getSorterTailReads();

        Optional<DebugReadTT> previousReadOpt = readList
                .stream()
                .filter(debugOperation -> debugOperation.getHashRequested() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {

            TranspositionEntry entryRead = entry.clone();

            readList.add(new DebugReadTT()
                    .setHashRequested(hashRequested)
                    .setEntry(entryRead)
                    .setMove(hashRequested == entry.getHash() ? readMove(entry) : DebugReadTT.UNKNOWN));
        }
    }

    String readMove(TranspositionEntry entry) {
        if (entry.getMove() == 0) {
            return DebugReadTT.NO_MOVE;
        }
        for (Move move : game.getPossibleMoves()) {
            if (move.getZobristHash() == entry.getMove()) {
                return move.coordinateEncoding();
            }
        }
        return DebugReadTT.UNKNOWN;
    }
}
