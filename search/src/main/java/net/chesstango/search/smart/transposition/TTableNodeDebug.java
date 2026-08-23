package net.chesstango.search.smart.transposition;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugNodeTT;

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

        List<DebugNodeTT> debugNodeTTOps = currentNode.getTranspositionOperations();

        Optional<DebugNodeTT> previousReadOpt = debugNodeTTOps
                .stream()
                .filter(debugNodeTT -> debugNodeTT.getEntry().getHash() == hashRequested)
                .findFirst();

        if (previousReadOpt.isEmpty()) {
            TranspositionEntry entryRead = entry.clone();

            String moveStr = readMove(entry);

            DebugNodeTT debugNodeTT = new DebugNodeTT();

            debugNodeTT.setOperation(DebugNodeTT.Operation.READ);

            debugNodeTT.setEntry(entryRead);

            debugNodeTT.setMove(moveStr);

            debugNodeTTOps.add(debugNodeTT);
        }
    }

    void trackWriteTranspositionEntry(TranspositionEntry entry) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        List<DebugNodeTT> debugNodeTTOps = currentNode.getTranspositionOperations();

        TranspositionEntry entryWrite = entry.clone();

        String moveStr = readMove(entry);

        DebugNodeTT debugNodeTT = new DebugNodeTT();

        debugNodeTT.setOperation(DebugNodeTT.Operation.WRITE);

        debugNodeTT.setEntry(entryWrite);

        debugNodeTT.setMove(moveStr);

        debugNodeTTOps.add(debugNodeTT);

    }

    String readMove(TranspositionEntry entry) {
        if (entry.getMove() == 0) {
            return DebugNodeTT.NO_MOVE;
        }

        for (Move move : game.getPossibleMoves()) {
            if (move.binaryEncoding() == entry.getMove()) {
                return move.coordinateEncoding();
            }
        }

        return DebugNodeTT.UNKNOWN;
    }
}
