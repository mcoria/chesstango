package chess.movesgenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Square;
import chess.moves.Move;


/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorResult {
	
	private Collection<Move> moveContainer;
	
	private long affectedByContainer;

	public MoveGeneratorResult() {
		moveContainer = createContainer(); 
	}
	
	public Collection<Move> getPseudoMoves(){
		return moveContainer;
	}
	

	public long getAffectedBy() {
		return affectedByContainer;
	}
	
	public void affectedByContainerAdd(Square key) {
		affectedByContainer |= key.getPosicion();
	}		
	
	public void moveContainerAdd(Move move) {
		moveContainer.add(move);
	}	

	
	private static Collection<Move> createContainer(){
		return new ArrayList<Move>() {
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		buffer.append(moveContainer.toString());
		return buffer.toString();
	}
}
