package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugNodeReadTT;
import net.chesstango.search.smart.debug.model.DebugNodeWriteTT;

import java.util.List;
import java.util.Optional;

/**
 * @author Mauricio Coria
 */
public class TTableNodeDebug implements TTable, Acceptor {

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
        tTable.save(entry);
        trackWriteTranspositionEntry(entry);
    }


    void trackReadTranspositionEntry(long hashRequested, TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugNodeReadTT> debugNodeReadTTOps = currentNode.getTranspositionNodeReads();

        Optional<DebugNodeReadTT> previousReadOpt = debugNodeReadTTOps
                .stream()
                .filter(debugNodeReadTT -> debugNodeReadTT.getHashRequested() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {
            DebugNodeReadTT debugNodeReadTT = new DebugNodeReadTT()
                    .setHashRequested(hashRequested)
                    .setEntry(entry.clone())
                    .setMove(hashRequested == entry.getDraft() ? readMove(entry) : DebugNodeReadTT.UNKNOWN);

            debugNodeReadTTOps.add(debugNodeReadTT);
        }
    }

    void trackWriteTranspositionEntry(TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugNodeWriteTT> debugNodeReadTTOps = currentNode.getTranspositionNodeWrites();

        TranspositionEntry entryWrite = entry.clone();

        String moveStr = readMove(entry);

        DebugNodeWriteTT debugNodeReadTT = new DebugNodeWriteTT();

        debugNodeReadTT.setEntry(entryWrite);

        debugNodeReadTT.setMove(moveStr);

        debugNodeReadTTOps.add(debugNodeReadTT);

    }

    String readMove(TranspositionEntry entry) {
        if (entry.getMove() == 0) {
            return DebugNodeReadTT.NO_MOVE;
        }

        for (Move move : game.getPossibleMoves()) {
            if (move.binaryEncoding() == entry.getMove()) {
                return move.coordinateEncoding();
            }
        }

        return DebugNodeReadTT.UNKNOWN;
    }
}
