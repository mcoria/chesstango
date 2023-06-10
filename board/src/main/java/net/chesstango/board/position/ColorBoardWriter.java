package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ColorBoardWriter {
    void swapPositions(Color color, Square remove, Square add);

    void addPositions(PiecePositioned position);

    void removePositions(PiecePositioned position);
}
