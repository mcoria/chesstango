package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;
import net.chesstango.search.smart.features.debug.model.DebugOperationEval;
import net.chesstango.search.smart.features.debug.model.DebugOperationTT;
import net.chesstango.search.smart.features.killermoves.KillerMoves;
import net.chesstango.search.smart.features.transposition.TranspositionEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Mauricio Coria
 */
public class MoveSorterDebug implements MoveSorter, SearchByCycleListener {
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

        trackComparatorKillerMoves(currentPly, currentNode, sortedMoves);

        searchTracker.sortingOFF();

        return sortedMoves;
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
