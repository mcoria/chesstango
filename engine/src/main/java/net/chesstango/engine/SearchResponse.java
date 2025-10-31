package net.chesstango.engine;

import net.chesstango.board.moves.Move;

import java.io.Serializable;

/**
 * @author Mauricio Coria
 */
public interface SearchResponse extends Serializable {
    Move getMove();
}
