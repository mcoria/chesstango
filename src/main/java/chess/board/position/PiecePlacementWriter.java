package chess.board.position;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;

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