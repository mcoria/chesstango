package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.alphabeta.debug.model.DebugNode;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationEval;
import net.chesstango.search.smart.alphabeta.debug.model.DebugOperationTT;
import net.chesstango.search.smart.sorters.MoveSorter;
import net.chesstango.search.smart.transposition.TranspositionEntry;

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
    private Game game;
    private Move[] killerMovesTableA;
    private Move[] killerMovesTableB;

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
        searchTracker = context.getSearchTracker();
        killerMovesTableA = context.getKillerMovesTableA();
        killerMovesTableB = context.getKillerMovesTableB();
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {

        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves(currentPly);

        trackSortedMoves(currentPly, convertMoveListToStringList(sortedMoves));

        trackComparatorsReads(sortedMoves);

        trackKillerMoves(currentPly);

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    /**
     * Este metodo deberia moverse una vez tengamos el wrapper de killer move tables
     */
    private void trackKillerMoves(int currentPly) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentPly > 0) {
            if (killerMovesTableA != null && killerMovesTableB != null) {
                if (killerMovesTableA[currentPly - 1] != null) {
                    currentNode.setSorterKmA(killerMovesTableA[currentPly - 1]);
                }
                if (killerMovesTableB[currentPly - 1] != null) {
                    currentNode.setSorterKmA(killerMovesTableB[currentPly - 1]);
                }
            }
        }
    }

    public void trackSortedMoves(int currentPly, List<String> sortedMovesStr) {
        DebugNode currentNode = searchTracker.getCurrentNode();
        if (currentNode != null) {
            currentNode.setSortedPly(currentPly);
            currentNode.setSortedMoves(sortedMovesStr);
        }
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }

    public void trackComparatorsReads(Iterable<Move> moves) {
        List<DebugOperationTT> sorterReads = searchTracker.getCurrentNode().getSorterReads();

        List<DebugOperationEval> evalCacheReads = searchTracker.getCurrentNode().getEvalCacheReads();


        // Transposition Head Access
        final long positionHash = game.getChessPosition().getZobristHash();
        for (Move move : moves) {
            final String moveStr = simpleMoveEncoder.encode(move);
            final short moveEncoded = move.binaryEncoding();

            sorterReads.stream()
                    .filter(debugNodeTT -> positionHash == debugNodeTT.getEntry().getHash())
                    .filter(debugNodeTT -> moveEncoded == TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()))
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


        // Transposition Tail Access
        for (Move move : moves) {
            final long zobristHashMove = game.getChessPosition().getZobristHash(move);
            final String moveStr = simpleMoveEncoder.encode(move);

            sorterReads.stream()
                    .filter(debugNodeTT -> zobristHashMove == debugNodeTT.getEntry().getHash())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            evalCacheReads.stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(moveStr));
        }

        /**
         * INVESTIGAR
         */
        sorterReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove("UNKNOWN"));
    }
}
