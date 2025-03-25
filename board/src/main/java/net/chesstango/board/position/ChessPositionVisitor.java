package net.chesstango.board.position;

/**
 * @author Mauricio Coria
 */
public interface ChessPositionVisitor {

    void visit(ChessPosition chessPosition);

    void visit(SquareBoardWriter squareBoard);

    void visit(PositionStateWriter positionState);

    void visit(KingSquareWriter kingSquareWriter);

    void visit(MoveCacheBoardWriter moveCache);

    void visit(ZobristHashWriter hash);

}
