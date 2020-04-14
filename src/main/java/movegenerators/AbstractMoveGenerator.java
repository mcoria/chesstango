package movegenerators;

import java.util.Collection;

import chess.Board;
import chess.Move;

public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected Board tablero;
	
	protected MoveFilter filter = (Collection<Move> moves, Move move) -> moves.add(move);

	@Override
	public void setTablero(Board tablero) {
		this.tablero = tablero;
	}

	@Override
	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}

}
