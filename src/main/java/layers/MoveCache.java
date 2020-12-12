package layers;

import java.util.Collection;

import chess.Move;
import chess.Square;

public class MoveCache {
	
	@SuppressWarnings("unchecked")
	protected Collection<Move> pseudoMoves[] = new Collection[64];
	
	protected long affects[] = new long[64];
	protected long affectedBy[] = new long[64];
	
	
	public Collection<Move> getPseudoMoves(Square key) {
		return pseudoMoves[key.toIdx()];
	}
	
	public void setPseudoMoves(Square key, Collection<Move> pseudoMovesCollection, long affectedByCollection) {
		pseudoMoves[key.toIdx()] = pseudoMovesCollection;
		affectedBy[key.toIdx()] = affectedByCollection;
		for(int i = 0; i < 64; i++){
			if( (affectedByCollection & (1L << i))  != 0 ) {
				 affects[i] |= key.getPosicion();
			}
		}
	}

	public void clearPseudoMoves(Square key) {
		// Affects squares
		long affectsSquares = affects[key.toIdx()];
		for(int i = 0; i < 64; i++){
			if( (affectsSquares & (1L << i))  != 0 ) {
				pseudoMoves[i] = null;
			}
		}
		affects[key.toIdx()] = 0;
		
		// AffectedBy squares
		long affectsBySquares = affectedBy[key.toIdx()];
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= ~key.getPosicion();
			}
		}
		affectedBy[key.toIdx()] = 0;		
		
		//Current square pseudomove
		pseudoMoves[key.toIdx()] = null;
	}

}
