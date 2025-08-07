package net.chesstango.board.position;

import net.chesstango.board.Color;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.gardel.PositionExporter;

import java.util.Iterator;

/**
 * @author Mauricio Coria
 *
 */
public interface PositionReader extends SquareBoardReader, BitBoardReader, PositionStateReader, ZobristHashReader, KingSquareReader, PositionExporter {

	Color getColor(Square square);

	Iterator<PiecePositioned> iteratorAllPieces();

}
