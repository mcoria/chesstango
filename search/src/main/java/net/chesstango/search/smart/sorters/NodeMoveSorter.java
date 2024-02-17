package net.chesstango.search.smart.sorters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.comparators.MoveComparator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorter implements MoveSorter, SearchByCycleListener {
    private final Predicate<Move> filter;

    private Game game;

    @Getter
    @Setter
    private MoveComparator moveComparator;

    public NodeMoveSorter() {
        this(move -> true);
    }

    public NodeMoveSorter(Predicate<Move> filter) {
        this.filter = filter;
    }

    @Override
    public Iterable<Move> getOrderedMoves(final int currentPly) {
        MoveContainerReader moves = game.getPossibleMoves();

        List<Move> moveList = new ArrayList<>(moves.size());
        for (Move move : moves) {
            if (filter.test(move)) {
                moveList.add(move);
            }
        }

        Map<Short, Long> moveToZobrist = new HashMap<>();
        moveComparator.beforeSort(currentPly, moveToZobrist);
        moveList.sort(moveComparator.reversed());
        moveComparator.afterSort(moveToZobrist);

        return moveList;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
    }
}
