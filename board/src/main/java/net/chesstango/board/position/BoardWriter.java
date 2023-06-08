package net.chesstango.board.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface BoardWriter {

	void setPosition(PiecePositioned entry);

	void setPiece(Square square, Piece piece);

	void setEmptyPosition(PiecePositioned piecePositioned);

	void setEmptySquare(Square square);

	void move(PiecePositioned from, PiecePositioned to);

}