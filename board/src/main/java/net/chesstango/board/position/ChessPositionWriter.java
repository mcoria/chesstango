package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ChessPositionWriter {

    SquareBoard getSquareBoard();

    BitBoard getBitBoard();

    KingSquare getKingSquare();

    MoveCacheBoard getMoveCache();

    PositionState getPositionState();

    ZobristHash getZobrist();
}
