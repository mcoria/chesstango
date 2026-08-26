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
import net.chesstango.search.smart.debug.model.DebugWriteTT;

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

        List<DebugReadTT> debugReadTTOps = currentNode.getTranspositionNodeReads();

        Optional<DebugReadTT> previousReadOpt = debugReadTTOps
                .stream()
                .filter(debugReadTT -> debugReadTT.getHashRequested() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {
            DebugReadTT debugReadTT = new DebugReadTT()
                    .setHashRequested(hashRequested)
                    .setEntry(entry.clone())
                    .setMove(hashRequested == entry.getHash() ? readMove(entry) : DebugReadTT.UNKNOWN);

            debugReadTTOps.add(debugReadTT);
        }
    }

    void trackWriteTranspositionEntry(TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugWriteTT> debugNodeReadTTOps = currentNode.getTranspositionNodeWrites();

        TranspositionEntry entryWrite = entry.clone();

        String moveStr = readMove(entry);

        DebugWriteTT debugNodeReadTT = new DebugWriteTT();

        debugNodeReadTT.setEntry(entryWrite);

        debugNodeReadTT.setMove(moveStr);

        debugNodeReadTTOps.add(debugNodeReadTT);

    }

    String readMove(TranspositionEntry entry) {
        if (entry.getMove() == 0) {
            return DebugReadTT.NO_MOVE;
        }

        for (Move move : game.getPossibleMoves()) {
            if (move.binaryEncoding() == entry.getMove()) {
                return move.coordinateEncoding();
            }
        }

        return DebugReadTT.UNKNOWN;
    }
}
