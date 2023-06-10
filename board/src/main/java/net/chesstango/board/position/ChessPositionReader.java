package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessRepresentationBuilder;
import net.chesstango.board.iterators.SquareIterator;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader extends BoardReader, PositionStateReader, ZobristHashReader, KingCacheBoardReader {

	Color getColor(Square square);

	long getColorPositions(Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWithoutKing(Color color);

	Iterator<PiecePositioned> iteratorAllPieces();

	void constructChessPositionRepresentation(ChessRepresentationBuilder<?> builder);
}
