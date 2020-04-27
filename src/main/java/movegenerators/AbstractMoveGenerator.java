package movegenerators;

import java.util.Collection;

import chess.Color;
import chess.DummyBoard;
import chess.Move;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected DummyBoard tablero;
	
	protected Collection<Move> moveContainer;
	
	protected MoveFilter filter = (Move move) -> true;
	
	public AbstractMoveGenerator(Color color) {
		this.color = color;
	}

	@Override
	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	@Override
	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}

	@Override
	public void setMoveColector(Collection<Move> moveContainer){
		this.moveContainer = moveContainer;
	}
}
