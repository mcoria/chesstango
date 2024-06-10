package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface SquareBoardCommand {
    void doMove(SquareBoardWriter squareBoard);

    void undoMove(SquareBoardWriter squareBoard);
}
