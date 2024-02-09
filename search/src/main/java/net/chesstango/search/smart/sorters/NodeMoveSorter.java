package net.chesstango.search.smart.sorters;


import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;
import net.chesstango.search.smart.sorters.comparators.MoveComparator;
import net.chesstango.search.smart.sorters.comparators.TranspositionHeadMoveComparator;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class NodeMoveSorter implements MoveSorter, SearchByCycleListener {

    private final Predicate<Move> filter;

    @Setter
    @Getter
    private MoveComparator moveComparator;

    private Game game;

    public NodeMoveSorter() {
        this(move -> true);
    }

    public NodeMoveSorter(Predicate<Move> filter) {
        this.filter = filter;
    }

    @Override
    public List<Move> getSortedMoves() {
        List<Move> moveList = new LinkedList<>();

        MoveContainerReader moves = game.getPossibleMoves();
        for (Move move : moves) {
            if (filter.test(move)) {
                moveList.add(move);
            }
        }

        moveList.sort(moveComparator.reversed());

        return moveList;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
    }

    @Override
    public void afterSearch() {
    }

}
