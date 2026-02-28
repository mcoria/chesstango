package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.position.GameHistoryRecord;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;

import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class SearchTracker {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    private DebugNode rootNode;

    @Getter
    private DebugNode currentNode;

    @Setter
    private Game game;


    public DebugNode newNode(DebugNode.NodeTopology topology, int currentPly) {

        DebugNode newNode;
        if (DebugNode.NodeTopology.ROOT.equals(topology)) {
            newNode = createRootNode();
            rootNode = newNode;
        } else {
            newNode = createRegularNode(topology, currentPly);
            currentNode.getChildNodes().add(newNode);
        }

        newNode.setZobristHash(game.getPosition().getZobristHash());
        if (game.getHistory().peekLastRecord() != null) {
            GameHistoryRecord gameHistoryRecord = game.getHistory().peekLastRecord();
            newNode.setSelectedMove(gameHistoryRecord.playedMove());
        }

        currentNode = newNode;

        return currentNode;
    }


    protected DebugNode createRootNode() {
        assert currentNode == null;
        DebugNode newNode = new DebugNode();
        newNode.setTopology(DebugNode.NodeTopology.ROOT);
        newNode.setPly(0);
        newNode.setFen(game.getPosition().toString());
        return newNode;
    }

    protected DebugNode createRegularNode(DebugNode.NodeTopology topology, int currentPly) {
        DebugNode newNode = new DebugNode();
        newNode.setTopology(topology);
        newNode.setPly(currentPly);
        newNode.setParent(currentNode);
        return newNode;
    }

    public void save() {
        trackTranspositionsAccess();
        currentNode.validate();
        currentNode = currentNode.getParent();
    }

    public void reset() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        rootNode = null;
    }

    public DebugNode getRootNode() {
        if (currentNode != null) {
            throw new RuntimeException("Still searching?");
        }
        return rootNode;
    }


    void trackTranspositionsAccess() {
        List<DebugOperationTT> entryReads = currentNode.getEntryRead();
        List<DebugOperationTT> entryWrites = currentNode.getEntryWrite();

        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final short moveEncoded = move.binaryEncoding();

            entryReads.stream()
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            entryWrites.stream()
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

        }

        /**
         * Deberian ser escrituras de nodos HORIZON donde QS search arroja el Standing Pat como mejor evaluacion
         */

        entryReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(debugNodeTT.getEntry().getMove() == 0 ? "NO_MOVE" : "UNKNOWN"));

        entryWrites
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(debugNodeTT.getEntry().getMove() == 0 ? "NO_MOVE" : "UNKNOWN"));
    }
}
