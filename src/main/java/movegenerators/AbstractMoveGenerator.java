package movegenerators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {

	@Override
	public Collection<Move> getLegalMoves(DummyBoard tablero, Map.Entry<Square, Pieza> origen) {
		Collection<Move> moves = createMoveContainer();
		Pieza currentPieza = origen.getValue();
		BoardState boardState = tablero.getBoardState();
		Collection<Move> pseudoMoves = getPseudoMoves(tablero, origen);
		for (Move move : pseudoMoves) {
			move.execute(tablero, boardState);
			if(! tablero.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
			move.undo(tablero, boardState);
			boardState.restoreState();
		}
		return moves;
	}
	
	public abstract Collection<Move> getPseudoMoves(DummyBoard board, Map.Entry<Square, Pieza> origen);	
	

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
	

}
