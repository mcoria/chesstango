package movegenerators;

import chess.PosicionPieza;
import chess.Square;
import layers.PosicionPiezaBoard;

public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(PosicionPiezaBoard tablero);
}
