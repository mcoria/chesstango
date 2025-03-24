package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface KingSquareVisitor {

    void doMove(KingSquareWriter kingSquareWriter);

    void undoMove(KingSquareWriter kingSquareWriter);

}
