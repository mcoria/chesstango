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
public interface ChessPositionReader extends SquareBoardReader, ColorBoardReader, PositionStateReader, ZobristHashReader, KingSquareReader {

	Color getColor(Square square);

	Iterator<PiecePositioned> iteratorAllPieces();

	void constructChessPositionRepresentation(ChessRepresentationBuilder<?> builder);
}
