package net.chesstango.board.position;

import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 */
public interface ChessPosition extends ChessPositionReader {
    void init();

    void acceptForDo(Move move);

    void acceptForUndo(Move move);

    SquareBoard getSquareBoard();

    BitBoard getBitBoard();

    KingSquare getKingSquare();

    MoveCacheBoard getMoveCache();

    PositionState getPositionState();

    ZobristHash getZobrist();
}
