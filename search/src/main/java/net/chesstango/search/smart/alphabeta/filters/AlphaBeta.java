package net.chesstango.search.smart.alphabeta.filters;

import lombok.Getter;
import lombok.Setter;
import net.chesstango.board.moves.Move;
import net.chesstango.search.smart.sorters.MoveSorter;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public class AlphaBeta extends AlphaBetaAbstract {

    @Setter
    @Getter
    private MoveSorter moveSorter;

    @Override
    protected List<Move> getSortedMoves() {
        return moveSorter.getSortedMoves();
    }

}
