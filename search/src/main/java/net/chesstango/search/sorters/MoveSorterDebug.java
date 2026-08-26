package net.chesstango.search.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.Acceptor;
import net.chesstango.search.Visitor;
import net.chesstango.search.smart.debug.DebugNodeTracker;
import net.chesstango.search.smart.debug.model.DebugNode;

import java.util.ArrayList;
import java.util.List;

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

        return sortedMoves;
    }

    private List<String> convertMoveListToStringList(Iterable<Move> moves) {
        List<String> sortedMovesStr = new ArrayList<>();
        for (Move move : moves) {
            sortedMovesStr.add(move.coordinateEncoding());
        }
        return sortedMovesStr;
    }

}
