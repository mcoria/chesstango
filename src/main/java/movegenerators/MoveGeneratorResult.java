package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Move;
import chess.Square;

public class MoveGeneratorResult {
	
	protected Collection<Move> moveContainer;
	
	protected Collection<Square> affectedByContainer;

	private boolean saveMovesInCache;

	public MoveGeneratorResult() {
		moveContainer = createContainer(); 
		affectedByContainer = createContainer();
	}
	
	public Collection<Move> getPseudoMoves(){
		return moveContainer;
	}
	

	public Collection<Square> getAffectedBy() {
		return affectedByContainer;
	}
	
	
	public boolean isSaveMovesInCache(){
		return this.saveMovesInCache;
	}

	public void setSaveMovesInCache(boolean flag){
		this.saveMovesInCache = flag;
	}
	
	public void affectedByContainerAdd(Square key) {
		affectedByContainer.add(key);
	}		
	
	public void moveContainerAdd(Move move) {
		moveContainer.add(move);
	}	
	
	private static <T> Collection<T> createContainer(){
		return new ArrayList<T>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 2237718042714336104L;

			@Override
			public String toString() {
				StringBuffer buffer = new StringBuffer(); 
				for (T move : this) {
					buffer.append(move.toString() + "\n");
				}
				return buffer.toString();
			}
		};
	}
	
}
