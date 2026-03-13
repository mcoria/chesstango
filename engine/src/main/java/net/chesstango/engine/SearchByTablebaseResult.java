package net.chesstango.engine;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public record SearchByTablebaseResult(Move move, int syzygyResult, long timeSearching) implements SearchResponse {

    @Override
    public long getTimeSearching() {
        return timeSearching;
    }
}
