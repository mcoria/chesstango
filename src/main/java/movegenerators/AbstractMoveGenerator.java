package movegenerators;

import chess.BoardCache;
import chess.Color;
import chess.DummyBoard;
import chess.Move;

// Y si tenemos objetos prototipos de movimientos y lo clonamos de ser validos?
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected final Color color;
	
	protected DummyBoard tablero;
	
	protected MoveFilter filter = (Move move) -> true;
	
	protected BoardCache boardCache;
	
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

	public void setBoardCache(BoardCache boardCache) {
		this.boardCache = boardCache;
	}

}
