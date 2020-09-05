package movegenerators;

import java.util.Collection;

import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	void calculatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(DummyBoard tablero);
	
	Collection<Move> getPseudoMoves();

	Collection<Square> getAffectedBy();

	boolean saveMovesInCache();

}
