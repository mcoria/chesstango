package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface KingSquareCommand {

    void doMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);

}
