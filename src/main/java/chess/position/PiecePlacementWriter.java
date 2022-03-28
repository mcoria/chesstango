package chess.position;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

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