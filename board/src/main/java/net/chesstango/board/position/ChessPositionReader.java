package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.builders.ChessPositionBuilder;
import net.chesstango.board.moves.Move;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader extends SquareBoardReader, BitBoardReader, PositionStateReader, ZobristHashReader, KingSquareReader {

	Color getColor(Square square);

	Iterator<PiecePositioned> iteratorAllPieces();

	void constructChessPositionRepresentation(ChessPositionBuilder<?> builder);

	long getZobristHash(Move move);
}
