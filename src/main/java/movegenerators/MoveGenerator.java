package movegenerators;

import chess.DummyBoard;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	MoveGeneratorResult calculatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(DummyBoard tablero);
}
