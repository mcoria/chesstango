package net.chesstango.board.moves;

import net.chesstango.board.position.KingSquareWriter;

/**
 * @author Mauricio Coria
 */
public interface MoveKingExecutor extends MoveExecutor {

    void doMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);

}
