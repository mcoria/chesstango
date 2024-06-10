package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ZobristHashCommand {
    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);
}
