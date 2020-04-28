package movegenerators;

import java.util.Collection;

import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	//La generacion de movimientos lo obtenemos en getGeneratedMoves
	void generateMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(DummyBoard tablero);

	void setFilter(MoveFilter filter);
	
	void setMoveColector(Collection<Move> moveContainer);

	void setSquaresColector(Collection<Square> origenSquaresListener);
	
	// List<Move> getGeneratedMoves
	
	// long getAffectedPositions

}
