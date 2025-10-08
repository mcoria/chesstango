package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.board.representations.move.SimpleMoveEncoder;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.features.debug.SearchTracker;
import net.chesstango.search.smart.features.debug.model.DebugNode;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        searchTracker.sortingON();

        Iterable<Move> sortedMoves = moveSorterImp.getOrderedMoves(currentPly);

        DebugNode currentNode = searchTracker.getCurrentNode();
        currentNode.setSortedPly(currentPly);
        currentNode.setSortedMoves(convertMoveListToStringList(sortedMoves));

        searchTracker.sortingOFF();

        return sortedMoves;
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(simpleMoveEncoder.encode(move));
        }
        return sortedMovesStr;
    }
}
