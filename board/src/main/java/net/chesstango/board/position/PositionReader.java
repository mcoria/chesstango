package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.gardel.PositionBuilder;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionReader extends SquareBoardReader, BitBoardReader, PositionStateReader, ZobristHashReader, KingSquareReader {

	Color getColor(Square square);

	Iterator<PiecePositioned> iteratorAllPieces();

	void constructChessPositionRepresentation(PositionBuilder<?> builder);
}
