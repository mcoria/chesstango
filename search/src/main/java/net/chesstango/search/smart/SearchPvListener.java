package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface SearchPvListener {
    void beforePVSearch(int bestValue);

    void afterPVSearch(List<Move> principalVariation);
}
