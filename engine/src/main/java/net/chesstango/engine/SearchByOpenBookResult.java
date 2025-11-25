package net.chesstango.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;

/**
 * @author Mauricio Coria
 */
public record SearchByOpenBookResult(Move move, PolyglotEntry polyglotEntry) implements SearchResponse {
}
