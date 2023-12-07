package net.chesstango.search.smart.alphabeta.filters;

import lombok.Setter;
import net.chesstango.board.Game;
import net.chesstango.board.moves.Move;
import net.chesstango.search.SearchMoveResult;
import net.chesstango.search.smart.SearchContext;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta extends AlphaBetaAbstract {

    @Setter
    private MoveSorter moveSorter;

    @Override
    protected List<Move> getSortedMoves() {
        return moveSorter.getSortedMoves();
    }

    @Override
    public void beforeSearch(Game game) {
        this.game = game;
    }

    @Override
    public void afterSearch(SearchMoveResult result) {
    }

    @Override
    public void beforeSearchByDepth(SearchContext context) {
    }

    @Override
    public void afterSearchByDepth(SearchMoveResult result) {
    }

    @Override
    public void reset() {
    }

    @Override
    public void stopSearching() {
    }
}
