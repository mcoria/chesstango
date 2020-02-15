package movegenerators;

import java.util.Collection;
import java.util.Map;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public interface MoveGenerator {

	public Collection<Move> getLegalMoves(DummyBoard board, Map.Entry<Square, Pieza> origen);

	public boolean puedeCapturarRey(DummyBoard board, Map.Entry<Square, Pieza> origen, Square kingSquare);

}
