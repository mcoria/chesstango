package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCache {
	
	@SuppressWarnings("unchecked")
	protected Collection<Move> pseudoMoves[] = new Collection[64];
	
	@SuppressWarnings("unchecked")
	protected Collection<Square> affects[] = new Collection[64];
	

	public MoveCache() {
		for (int i = 0; i < 64; i++) {
			affects[i] = new ArrayList<Square>(); 
		}
	}
	
	public Collection<Move> getPseudoMoves(Square key) {
		return pseudoMoves[key.ordinal()];
	}
	
	public void setPseudoMoves(Square key, Collection<Move> container) {
		pseudoMoves[key.ordinal()] = container;
	}
	
	public void setAffectedBy(Square key, Collection<Square> origenSquaresListener) {
		for (Square square : origenSquaresListener) {
			 affects[square.ordinal()].add(key);
		}
	}

	public void clearPseudoMovesAffectedBy(Square key) {
		Collection<Square> affecteds = affects[key.ordinal()];
		for (Square square : affecteds) {
			pseudoMoves[square.ordinal()] = null;
		}
		affecteds.clear();
		pseudoMoves[key.ordinal()] = null;
	}

}
