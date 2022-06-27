package net.chesstango.board.position;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface PiecePlacementWriter {

	void setPosicion(PiecePositioned entry);

	void setPieza(Square square, Piece piece);

	void setEmptyPosicion(PiecePositioned captura);

	void setEmptySquare(Square square);

	void move(PiecePositioned from, PiecePositioned to);

}