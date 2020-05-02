package movegenerators;

import java.util.Collection;

import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	void generatePseudoMoves(PosicionPieza origen);

	boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	void setTablero(DummyBoard tablero);
	
	Collection<Move> getMoveContainer();

	Collection<Square> getAffectedBy();

}
