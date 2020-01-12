package movegenerators;

import java.util.Map;
import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

public interface MoveGenerator {

	public Set<Move> getLegalMoves(Board board, Map.Entry<Square, Pieza> origen);

	public Set<Move> getPseudoMoves(DummyBoard board, Map.Entry<Square, Pieza> origen);

	public boolean puedeCapturarRey(DummyBoard board, Map.Entry<Square, Pieza> origen, Square kingSquare);

}
