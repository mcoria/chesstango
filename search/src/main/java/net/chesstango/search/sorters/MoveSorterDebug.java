package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;
import net.chesstango.search.smart.debug.model.DebugOperationEval;
import net.chesstango.search.smart.debug.model.DebugOperationTT;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class MoveSorterDebug implements MoveSorter, Acceptor {

    @Setter
    @Getter
    private MoveSorter next;

    @Setter
    private DebugNodeTracker debugNodeTracker;

    @Setter
    private Game game;

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        DebugNode currentNode = debugNodeTracker.getCurrentNode();

        Iterable<Move> sortedMoves = next.getOrderedMoves(currentPly);

        currentNode.setSortedMoves(convertMoveListToStringList(sortedMoves));

        trackComparatorsEvalCacheReads(currentNode);

        trackComparatorsTranspositionReads(currentNode);

        return sortedMoves;
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(move.coordinateEncoding());
        }
        return sortedMovesStr;
    }

    void trackComparatorsEvalCacheReads(DebugNode debugNode) {
        List<DebugOperationEval> evalCacheReads = debugNode.getEvalCacheReads();

        for (Move move : game.getPossibleMoves()) {
            long zobristHashMove = move.getZobristHash();
            evalCacheReads
                    .stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(move.coordinateEncoding()));
        }
    }

    void trackComparatorsTranspositionReads(DebugNode debugNode) {
        List<DebugOperationTT> sorterReads = debugNode.getSorterReads();

        final long positionHash = game.getPosition().getZobristHash();
        for (Move move : game.getPossibleMoves()) {
            final String moveStr = move.coordinateEncoding();
            final long zobristHashMove = move.getZobristHash();
            final short moveEncoded = move.binaryEncoding();

            // Transposition Head Access
            sorterReads.stream()
                    .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                    .filter(debugNodeTT -> moveEncoded == debugNodeTT.getEntry().getMove())
                    .forEach(debugNodeTT -> debugNodeTT.setSortingMove(moveStr));

            // Transposition Tail Access
            sorterReads.stream()
                    .filter(debugNodeTT -> zobristHashMove == debugNodeTT.getEntry().getHash())
                    .forEach(debugNodeTT -> debugNodeTT.setSortingMove(moveStr));
        }

        /**
         * Estas son lecturas de TT que no tienen un movimiento asociado.
         */
        sorterReads
                .stream()
                .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getSortingMove()))
                .forEach(debugNodeTT -> debugNodeTT.setSortingMove("NO_MOVE"));

        /**
         * INVESTIGAR
         */
        sorterReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getSortingMove()))
                .forEach(debugNodeTT -> debugNodeTT.setSortingMove("UNKNOWN"));
    }
}
