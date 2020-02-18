package moveexecutors;

import java.util.Map;
import java.util.Map.Entry;

import chess.DummyBoard;
import chess.Pieza;
import chess.Square;

public interface MoveExecutor {

	void execute(DummyBoard board, Map.Entry<Square, Pieza> from, Map.Entry<Square, Pieza> to);

	void undo(DummyBoard board, Entry<Square, Pieza> from, Entry<Square, Pieza> to);

}
