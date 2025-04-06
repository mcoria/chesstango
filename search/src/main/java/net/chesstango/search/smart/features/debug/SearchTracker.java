package net.chesstango.search.smart.features.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationEval;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

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
        if (game.getPreviousState() != null) {
            newNode.setSelectedMove(game.getPreviousState().move());
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
        trackComparatorsTranspositionReads();

        trackComparatorsEvalCacheReads();

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


    private void trackComparatorsEvalCacheReads() {
        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = move.getZobristHash();

            evalCacheReads.stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(moveStr));
        }
    }

    public void trackComparatorsTranspositionReads() {
        List<DebugOperationTT> sorterReads = currentNode.getSorterReads();

        final long positionHash = game.getChessPosition().getZobristHash();
        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = move.getZobristHash();
            final short moveEncoded = move.binaryEncoding();

            // Transposition Head Access
            sorterReads.stream()
                    .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                    .filter(debugNodeTT -> moveEncoded == TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()))
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            // Transposition Tail Access
            sorterReads.stream()
                    .filter(debugNodeTT -> zobristHashMove == debugNodeTT.getEntry().getHash())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));
        }

        /**
         * Estas son lecturas de TT que no tienen un movimiento asociado.
         */
        sorterReads
                .stream()
                .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove("NO_MOVE"));

        /**
         * INVESTIGAR
         */
        sorterReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove("UNKNOWN"));
    }
}
