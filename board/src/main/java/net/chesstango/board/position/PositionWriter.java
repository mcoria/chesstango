package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface PositionWriter {

    SquareBoardWriter getSquareBoardWriter();

    BitBoardWriter getBitBoardWriter();

    KingSquareWriter getKingSquareWriter();

    MoveCacheBoardWriter getMoveCacheWriter();

    PositionStateWriter getPositionStateWriter();

    ZobristHashWriter getZobristWriter();
}
