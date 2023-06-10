package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ColorBoardReader {
    SquareIterator iteratorSquare(Color color);

    long getPositions(Color color);

    boolean isEmpty(Square square);

    Color getColor(Square square);
}
