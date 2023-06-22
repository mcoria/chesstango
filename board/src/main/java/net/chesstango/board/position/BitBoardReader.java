package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface BitBoardReader {
    SquareIterator iteratorSquare(Color color);

    long getPositions(Color color);

    long getAllPositions();

    long getEmptyPositions();

    long getBishopPositions();

    long getRookPositions();

    long getQueenPositions();

    boolean isEmpty(Square square);

    Color getColor(Square square);

}
