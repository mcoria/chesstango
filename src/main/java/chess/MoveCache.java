package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCache {
	
	@SuppressWarnings("unchecked")
	protected Collection<Move> tablero[] = new Collection[64];
	
	@SuppressWarnings("unchecked")
	protected Collection<Square> affects[] = new Collection[64];
	

	public MoveCache() {
		for (int i = 0; i < 64; i++) {
			affects[i] = new ArrayList<Square>(); 
		}
	}
	
	public Collection<Move> getMoveContainer(Square key) {
		return tablero[key.ordinal()];
	}
	
	public void setMoveContainer(Square key, Collection<Move> container) {
		tablero[key.ordinal()] = container;
	}
	
	public void setAffectedBy(Square key, Collection<Square> origenSquaresListener) {
		for (Square square : origenSquaresListener) {
			 affects[square.ordinal()].add(key);
		}
	}

	public void emptyContainversAffectedBy(Square key) {
		Collection<Square> affecteds = affects[key.ordinal()];
		for (Square square : affecteds) {
			tablero[square.ordinal()] = null;
		}
		affecteds.clear();
		tablero[key.ordinal()] = null;
	}

}
