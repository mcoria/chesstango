package net.chesstango.engine;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public class SearchByTablebaseResult implements SearchResponse {
    private final Move move;
    private final int syzygyResult;

    public SearchByTablebaseResult(Move move, int syzygyResult) {
        this.move = move;
        this.syzygyResult = syzygyResult;
    }

    @Override
    public Move getMove() {
        return move;
    }
}
