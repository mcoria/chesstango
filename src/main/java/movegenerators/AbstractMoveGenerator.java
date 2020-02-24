package movegenerators;

import java.util.Collection;

import chess.DummyBoard;
import chess.Move;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected DummyBoard tablero;
	
	protected Collection<Move> moveContainer;
	
	protected MoveFilter filter = (Collection<Move> moves, Move move) -> moves.add(move);

	@Override
	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	@Override
	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}
	
	@Override
	public void setMoveContainer(Collection<Move> moveContainer) {
		this.moveContainer = moveContainer;
	}	

}
