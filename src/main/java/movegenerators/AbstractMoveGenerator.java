package movegenerators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {
	
	protected DummyBoard tablero;
	
	protected MoveFilter filter = (Collection<Move> moves, Move move) -> moves.add(move);

	@Override
	public Collection<Move> getLegalMoves(Map.Entry<Square, Pieza> origen) {
		/*
		Collection<Move> moves = createMoveContainer();
		Pieza currentPieza = origen.getValue();
		BoardState boardState = tablero.getBoardState();
		Collection<Move> pseudoMoves = getPseudoMoves(origen);
		for (Move move : pseudoMoves) {
			move.execute(tablero, boardState);
			if(! tablero.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
			move.undo(tablero, boardState);
			boardState.restoreState();
		}*/
		return getPseudoMoves(origen);
	}
	
	protected abstract Collection<Move> getPseudoMoves(Map.Entry<Square, Pieza> origen);
	

	protected Collection<Move> createMoveContainer(){
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

	public void setTablero(DummyBoard tablero) {
		this.tablero = tablero;
	}

	public void setFilter(MoveFilter filter) {
		this.filter = filter;
	}
	

}
