package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ChessPositionCommand {
    void doMove(ChessPosition chessPosition);

    void undoMove(ChessPosition chessPosition);
}
