package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationEval;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class MoveSorterDebug implements MoveSorter {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    @Getter
    private MoveSorter moveSorterImp;

    @Setter
    private SearchTracker searchTracker;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        DebugNode currentNode = searchTracker.getCurrentNode();

        currentNode.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves(currentPly);

        currentNode.setSortedPly(currentPly);

        currentNode.setSortedMoves(convertMoveListToStringList(sortedMoves));

        currentNode.sortingOFF();

        trackComparatorsEvalCacheReads();

        trackComparatorsTranspositionReads();

        return sortedMoves;
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }

    void trackComparatorsEvalCacheReads() {
        DebugNode currentNode = searchTracker.getCurrentNode();

        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = move.getZobristHash();

            evalCacheReads.stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(moveStr));
        }
    }

    void trackComparatorsTranspositionReads() {
        DebugNode currentNode = searchTracker.getCurrentNode();

        List<DebugOperationTT> sorterReads = currentNode.getSorterReads();

        final long positionHash = game.getPosition().getZobristHash();
        for (Move move : game.getPossibleMoves()) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = move.getZobristHash();
            final short moveEncoded = move.binaryEncoding();

            // Transposition Head Access
            sorterReads.stream()
                    .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
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
