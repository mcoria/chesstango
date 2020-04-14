package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.Board;
import chess.Move;
import chess.Pieza;
import chess.Square;

public interface MoveGenerator {

	public void generateMoves(Map.Entry<Square, Pieza> origen, Collection<Move> moveContainer);

	public boolean puedeCapturarRey(Map.Entry<Square, Pieza> origen, Square kingSquare);
	
	public void setTablero(Board tablero);

	public void setFilter(MoveFilter filter);

}
