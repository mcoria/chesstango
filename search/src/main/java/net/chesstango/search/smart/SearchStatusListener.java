package net.chesstango.search.smart;

import net.chesstango.board.moves.Move;

import java.util.List;

/**
 * @author Mauricio Coria
 */
public interface SearchStatusListener {
    void info(int depth, int selDepth, List<Move> pv);
}
