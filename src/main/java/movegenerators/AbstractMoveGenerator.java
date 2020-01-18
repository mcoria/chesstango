package movegenerators;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import chess.BoardState;
import chess.DummyBoard;
import chess.Move;
import chess.Pieza;
import chess.Square;

//Template  Method Pattern GoF
public abstract class AbstractMoveGenerator implements MoveGenerator {

	@Override
	public Set<Move> getLegalMoves(DummyBoard tablero, BoardState boardState, Map.Entry<Square, Pieza> origen) {
		Set<Move> moves = new HashSet<Move>();
		Pieza currentPieza = origen.getValue();
		Set<Move> pseudoMoves = getPseudoMoves(tablero, boardState, origen);
		for (Move move : pseudoMoves) {
			move.execute(tablero, boardState);
			if(! tablero.isKingInCheck(currentPieza.getColor()) ) {
				moves.add(move);
			}
			move.undo(tablero, boardState);
		}
		return moves;
	}
	
	public Set<Move> getPseudoMoves(DummyBoard board, BoardState boardState, Map.Entry<Square, Pieza> origen){
		return getPseudoMoves(board, origen);
	}
	
	@Override
	public boolean puedeCapturarRey(DummyBoard dummyBoard, Map.Entry<Square, Pieza> origen, Square kingSquare) {
		Set<Move> pseudoMoves = getPseudoMoves(dummyBoard, origen);
		for (Move move : pseudoMoves) {
			if(kingSquare.equals(move.getTo().getKey())){
				return true;
			}
		}
		return false;
	}

	protected Set<Move> createMoveContainer(){
		return new HashSet<Move>() {
			/**
			 * 
			 */
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
