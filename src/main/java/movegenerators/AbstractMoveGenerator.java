package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.DummyBoard;
import chess.Move;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected DummyBoard tablero;
	
	protected MoveFilter filter = (Collection<Move> moves, Move move) -> moves.add(move);

	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}
	
	protected static Collection<Move> createMoveContainer(){
		return new ArrayList<Move>() {
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (Move move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}	

}
