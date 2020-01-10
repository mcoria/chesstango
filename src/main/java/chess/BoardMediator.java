package chess;

public interface BoardMediator {

	Pieza getPieza(Square from);

	void setEmptySquare(Square from);

	void setPieza(Square to, Pieza pieza);

}
