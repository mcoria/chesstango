package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardVisitor {
    void doMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);
}
