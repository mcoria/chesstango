package movegenerators;

import java.util.HashSet;
import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Square;

public class DummyMoveGenerator implements MoveGenerator{

	@Override
	public Set<Move> getLegalMoves(Board board, Square currentSquare) {
		return new HashSet<Move>();
	}

	@Override
	public Set<Move> getPseudoMoves(DummyBoard dummyBoard, Square casillero) {
		return new HashSet<Move>();
	}

	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Square casillero, Square kingSquare) {
		return false;
	}

}
