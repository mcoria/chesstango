package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveCacheBoardCommand {
    void doMove(MoveCacheBoardWriter moveCache);

    void undoMove(MoveCacheBoardWriter moveCache);
}
