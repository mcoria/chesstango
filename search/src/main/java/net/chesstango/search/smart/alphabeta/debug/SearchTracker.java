package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.transposition.TranspositionEntry;

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

    @Getter
    private boolean sorting;

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

        newNode.setZobristHash(game.getChessPosition().getZobristHash());
        if (game.getState().getPreviousState() != null) {
            newNode.setSelectedMove(game.getState().getPreviousState().getSelectedMove());
        }

        currentNode = newNode;

        return currentNode;
    }


    protected DebugNode createRootNode() {
        assert currentNode == null;
        DebugNode newNode = new DebugNode();
        newNode.setTopology(DebugNode.NodeTopology.ROOT);
        newNode.setPly(0);
        newNode.setFen(game.getChessPosition().toString());
        return newNode;
    }

    protected DebugNode createRegularNode(DebugNode.NodeTopology topology, int currentPly) {
        DebugNode newNode = new DebugNode();
        newNode.setTopology(topology);
        newNode.setPly(currentPly);
        newNode.setParent(currentNode);
        return newNode;
    }


    public void sortingON() {
        sorting = true;
    }

    public void sortingOFF() {
        sorting = false;
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


    private void trackTranspositionsAccess() {
        List<DebugOperationTT> entryReads = currentNode.getEntryRead();
        List<DebugOperationTT> entryWrites = currentNode.getEntryWrite();

        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final short moveEncoded = move.binaryEncoding();

            entryReads.stream()
                    .filter(debugNodeTT -> moveEncoded == TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()))
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            entryWrites.stream()
                    .filter(debugNodeTT -> moveEncoded == TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()))
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

        }

        /**
         * Deberian ser escrituras de nodos HORIZON donde QS search arroja el Standing Pat como mejor evaluacion
         */

        entryReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()) == 0 ? "NO_MOVE" : "UNKNOWN"));

        entryWrites
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove(TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()) == 0 ? "NO_MOVE" : "UNKNOWN"));
    }
}
