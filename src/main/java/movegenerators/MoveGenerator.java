package movegenerators;

import java.util.Set;

import chess.Board;
import chess.DummyBoard;
import chess.Move;
import chess.Square;

public interface MoveGenerator {

	public Set<Move> getLegalMoves(Board board, Square casillero);

	public Set<Move> getPseudoMoves(DummyBoard board, Square casillero);

	public boolean puedeCapturarRey(DummyBoard board, Square casillero, Square kingSquare);

}
