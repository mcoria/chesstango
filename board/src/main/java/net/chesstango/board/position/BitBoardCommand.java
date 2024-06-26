package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface BitBoardCommand {
    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);
}
