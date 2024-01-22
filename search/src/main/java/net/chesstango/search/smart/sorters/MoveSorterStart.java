package net.chesstango.search.smart.sorters;


import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MoveContainerReader;
import net.chesstango.search.smart.SearchByCycleContext;
import net.chesstango.search.smart.SearchByCycleListener;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Mauricio Coria
 */
public class MoveSorterStart implements MoveSorter, SearchByCycleListener {

    @Setter
    @Getter
    private MoveSorterElement nextMoveSorter;
    private Game game;

    private Predicate<Move> filter;

    public MoveSorterStart() {
        this.filter = (move -> true);
    }

    public MoveSorterStart(Predicate<Move> filter) {
        this.filter = filter;
    }

    @Override
    public List<Move> getSortedMoves() {
        List<Move> unsortedMoves = new LinkedList<>();
        List<Move> sortedMoves = new LinkedList<>();

        MoveContainerReader moves = game.getPossibleMoves();
        for (Move move :
                moves) {
            if (filter.test(move)) {
                unsortedMoves.add(move);
            }
        }


        nextMoveSorter.sort(unsortedMoves, sortedMoves);

        return sortedMoves;
    }

    @Override
    public void beforeSearch(SearchByCycleContext context) {
        game = context.getGame();
    }

    @Override
    public void afterSearch() {
    }
}
