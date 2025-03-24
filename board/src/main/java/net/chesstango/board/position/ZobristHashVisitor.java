package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ZobristHashVisitor {
    void doMove(ZobristHashWriter hash, ChessPositionReader chessPositionReader);

    void undoMove(ZobristHashWriter hash);
}
