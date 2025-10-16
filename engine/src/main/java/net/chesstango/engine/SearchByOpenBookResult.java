package net.chesstango.engine;

import net.chesstango.board.moves.Move;
import net.chesstango.piazzolla.polyglot.PolyglotEntry;

/**
 * @author Mauricio Coria
 */
public class SearchByOpenBookResult implements SearchResponse {
    private final Move move;
    private final PolyglotEntry polyglotEntry;

    public SearchByOpenBookResult(Move move, PolyglotEntry polyglotEntry) {
        this.move = move;
        this.polyglotEntry = polyglotEntry;
    }

    @Override
    public Move getMove() {
        return move;
    }
}
