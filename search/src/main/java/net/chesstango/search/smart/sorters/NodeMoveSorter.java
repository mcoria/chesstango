package net.chesstango.search.smart.sorters;

import lombok.Getter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.comparators.MoveComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorter implements MoveSorter, SearchByCycleListener {
    private final Predicate<Move> filter;

    private Game game;

    @Getter
    private MoveComparator moveComparator;

    public NodeMoveSorter() {
        this(move -> true);
    }

    public NodeMoveSorter(Predicate<Move> filter) {
        this.filter = filter;
    }

    @Override
    public Iterable<Move> getSortedMoves() {
        MoveContainerReader moves = game.getPossibleMoves();

        List<Move> moveList = new ArrayList<>(moves.size());
        for (Move move : moves) {
            if (filter.test(move)) {
                moveList.add(move);
            }
        }

        moveComparator.beforeSort();
        moveList.sort(moveComparator);
        moveComparator.afterSort();

        return moveList;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
    }

    @Override
    public void afterSearch() {
    }

    public void setMoveComparator(MoveComparator moveComparator) {
        this.moveComparator = moveComparator.reversed();
    }
}
