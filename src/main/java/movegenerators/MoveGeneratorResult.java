package movegenerators;

import java.util.ArrayList;
import java.util.Collection;

import chess.Square;
import moveexecutors.Move;


/**
 * @author Mauricio Coria
 *
 */
public class MoveGeneratorResult {
	
	private Collection<Move> moveContainer;
	
	private long affectedByContainer;

	private boolean saveMovesInCache;
	
	private boolean hasCapturePeonPasante;

	public MoveGeneratorResult() {
		moveContainer = createContainer(); 
	}
	
	public Collection<Move> getPseudoMoves(){
		return moveContainer;
	}
	

	public long getAffectedBy() {
		return affectedByContainer;
	}
	
	
	public boolean isSaveMovesInCache(){
		return this.saveMovesInCache;
	}

	public void setSaveMovesInCache(boolean flag){
		this.saveMovesInCache = flag;
	}
	
	public void affectedByContainerAdd(Square key) {
		affectedByContainer |= key.getPosicion();
	}		
	
	public void moveContainerAdd(Move move) {
		moveContainer.add(move);
	}	
	
	
	public void setHasCapturePeonPasante(boolean hasCapturePeonPasante) {
		this.hasCapturePeonPasante = hasCapturePeonPasante;
	}
	
	public boolean hasCapturePeonPasante() {
		return this.hasCapturePeonPasante;
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
	
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		buffer.append(moveContainer.toString());
		return buffer.toString();
	}
}
