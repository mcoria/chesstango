package movegenerators;

import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Square;

public interface MoveGenerator {
	
	public Set<Move> getLegalMoves(Board board, Square currentSquare);
	
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Square casillero);

	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare);

}
