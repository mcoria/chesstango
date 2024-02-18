package net.chesstango.search.smart.alphabeta.debug;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
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

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {

        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves(currentPly);

        searchTracker.trackSortedMoves(convertMoveListToStringList(sortedMoves));

        trackComparatorsReads(sortedMoves);

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
        searchTracker = context.getSearchTracker();
    }


    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }

    public void trackComparatorsReads(Iterable<Move> moves) {
        List<DebugOperationTT> sorterReads = searchTracker.getSorterReads();

        List<DebugOperationEval> evalCacheReads = searchTracker.getEvalCacheReads();

        final long positionHash = game.getChessPosition().getZobristHash();

        for (Move move : moves) {
            final long zobristHashMove = game.getChessPosition().getZobristHash(move);
            final String moveStr = simpleMoveEncoder.encode(move);
            final short moveEncoded = move.binaryEncoding();

            sorterReads.stream()
                    .filter(debugNodeTT -> zobristHashMove == debugNodeTT.getHashRequested())
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            sorterReads.stream()
                    .filter(debugNodeTT -> positionHash == debugNodeTT.getHashRequested()
                            && moveEncoded == TranspositionEntry.decodeBestMove(debugNodeTT.getEntry().getMovesAndValue()))
                    .forEach(debugNodeTT -> debugNodeTT.setMove(moveStr));

            evalCacheReads.stream()
                    .filter(debugOperationEval -> zobristHashMove == debugOperationEval.getHashRequested())
                    .forEach(debugOperationEval -> debugOperationEval.setMove(moveStr));
        }

        sorterReads
                .stream()
                .filter(debugNodeTT -> Objects.isNull(debugNodeTT.getMove()))
                .forEach(debugNodeTT -> debugNodeTT.setMove("HORIZONTE"));
    }
}
