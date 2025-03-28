package net.chesstango.board.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface BitBoardWriter {

    void swapPositions(Piece piece, Square remove, Square add);

    void addPosition(PiecePositioned position);

    void addPosition(Piece piece, Square square);

    void removePosition(PiecePositioned position);

    void removePosition(Piece piece, Square square);
}
