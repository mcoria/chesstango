package movegenerators;

import chess.DummyBoard;
import chess.Move;

public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected DummyBoard tablero;
	
	protected MoveFilter filter = (Move move) -> true;

	@Override
	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	@Override
	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}

}
