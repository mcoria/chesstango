package net.chesstango.search.smart.features.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationEval;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class TrapMoveSorter implements MoveSorter, SearchByCycleListener {
    private final SimpleMoveEncoder simpleMoveEncoder = new SimpleMoveEncoder();

    @Setter
    @Getter
    private MoveSorter moveSorterImp;
    private SearchTracker searchTracker;
    private KillerMoves killerMoves;
    private Game game;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
        searchTracker = context.getSearchTracker();
        killerMoves = context.getKillerMoves();
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves(currentPly);

        DebugNode currentNode = searchTracker.getCurrentNode();
        currentNode.setSortedPly(currentPly);
        currentNode.setSortedMoves(convertMoveListToStringList(sortedMoves));

        trackComparatorsTranspositionReads(currentNode, sortedMoves);

        trackComparatorsEvalCacheReads(currentNode, sortedMoves);

        trackComparatorKillerMoves(currentPly, currentNode, sortedMoves);

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    private void trackComparatorsEvalCacheReads(DebugNode currentNode, Iterable<Move> moves) {
        List<DebugOperationEval> evalCacheReads = currentNode.getEvalCacheReads();

        for (Move move : moves) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = game.getChessPosition().getZobristHash(move);

            evalCacheReads.stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(moveStr));
        }
    }

    public void trackComparatorsTranspositionReads(DebugNode currentNode, Iterable<Move> moves) {
        List<DebugOperationTT> sorterReads = currentNode.getSorterReads();

        final long positionHash = game.getChessPosition().getZobristHash();
        for (Move move : moves) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final long zobristHashMove = game.getChessPosition().getZobristHash(move);
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

    private void trackComparatorKillerMoves(int currentPly, DebugNode currentNode, Iterable<Move> moves) {
        if (killerMoves != null && currentPly > 1) {
            for (Move move : moves) {
                if (killerMoves.isKiller(move, currentPly)) {
                    if (currentNode.getSorterKmA() == null) {
                        currentNode.setSorterKmA(move);
                    } else if (currentNode.getSorterKmB() == null) {
                        currentNode.setSorterKmB(move);
                        break;
                    }
                }
            }
        }
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }
}
