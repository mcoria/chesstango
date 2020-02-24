package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public interface MoveGenerator {

	public void generateMoves(Map.Entry<Square, Pieza> origen);

	public boolean puedeCapturarRey(Map.Entry<Square, Pieza> origen, Square kingSquare);
	
	public void setTablero(DummyBoard tablero);

	public void setFilter(MoveFilter filter);
	
	public void setMoveContainer(Collection<Move> moveContainer); 

}
