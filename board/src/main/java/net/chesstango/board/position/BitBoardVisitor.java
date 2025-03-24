package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface BitBoardVisitor {
    void doMove(BitBoardWriter bitBoard);

    void undoMove(BitBoardWriter bitBoard);
}
