package movegenerators;

import chess.PosicionPieza;
import chess.Square;
import layers.DummyBoard;

public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(DummyBoard tablero);
}
