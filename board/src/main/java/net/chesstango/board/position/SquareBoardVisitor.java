package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface SquareBoardVisitor {
    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);
}
