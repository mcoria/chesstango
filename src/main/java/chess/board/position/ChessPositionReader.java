/**
 * 
 */
package chess.board.position;

import chess.board.Color;
import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.iterators.pieceplacement.PiecePlacementIterator;
import chess.board.iterators.square.SquareIterator;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionReader extends PiecePlacementReader {

	Color getTurnoActual();

	Color getColor(Square square);

	boolean isCastlingWhiteQueenAllowed();
	boolean isCastlingWhiteKingAllowed();

	boolean isCastlingBlackQueenAllowed();
	boolean isCastlingBlackKingAllowed();

	Square getKingSquare(Color color);

	Square getEnPassantSquare();

	long getPosiciones (Color color);

	SquareIterator iteratorSquare(Color color);

	SquareIterator iteratorSquareWhitoutKing(Color color);

	PiecePlacementIterator iteratorAllPieces();
	
	void constructBoardRepresentation(ChessPositionBuilder<?> builder);
}
