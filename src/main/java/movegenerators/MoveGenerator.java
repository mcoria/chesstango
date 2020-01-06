package movegenerators;

import java.util.Set;

import chess.DummyBoard;
import chess.Move;
import chess.Square;

public interface MoveGenerator {
	
	public Set<Move> getLegalMoves(DummyBoard tablero, Square currentSquare);
	
	public Set<Move> getPseudoMoves(DummyBoard tablero, Square casillero);

	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare);

}
