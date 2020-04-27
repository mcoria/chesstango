package movegenerators;

import java.util.Collection;

import chess.BoardCache;
import chess.DummyBoard;
import chess.Move;
import chess.PosicionPieza;
import chess.Square;

public interface MoveGenerator {

	//La generacion de movimientos lo obtenemos en getGeneratedMoves
	public void generateMoves(PosicionPieza origen, Collection<Move> moveContainer);

	public boolean puedeCapturarPosicion(PosicionPieza origen, Square square);
	
	public void setTablero(DummyBoard tablero);

	public void setFilter(MoveFilter filter);
	
	public void setBoardCache(BoardCache boardCache); 
	
	// List<Move> getGeneratedMoves
	
	// long getAffectedPositions

}
