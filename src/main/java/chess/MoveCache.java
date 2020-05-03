package chess;

import java.util.Collection;

public class MoveCache {
	
	@SuppressWarnings("unchecked")
	protected Collection<Move> pseudoMoves[] = new Collection[64];
	
	protected long affects[] = new long[64];
	
	
	public Collection<Move> getPseudoMoves(Square key) {
		return pseudoMoves[key.toIdx()];
	}
	
	public void setPseudoMoves(Square key, Collection<Move> container) {
		pseudoMoves[key.toIdx()] = container;
	}
	
	public void setAffectedBy(Square key, Collection<Square> origenSquaresListener) {
		for (Square square : origenSquaresListener) {
			 affects[square.toIdx()] |= key.getPosicion();
		}
	}

	public void clearPseudoMovesAffectedBy(Square key) {
		long affecteds = affects[key.toIdx()];
		for(int i = 0; i< 64; i++){
			if( (affecteds & (1L << i))  != 0 ) {
				pseudoMoves[i] = null;
			}
		}
		affects[key.toIdx()] = 0;
		pseudoMoves[key.toIdx()] = null;
	}

}
