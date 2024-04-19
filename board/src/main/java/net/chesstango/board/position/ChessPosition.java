package net.chesstango.board.position;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface ChessPosition extends ChessPositionReader {
    void init();

    void doMove(Move move);

    void undoMove(Move move);
}
