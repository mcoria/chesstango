package layers;

import java.util.Collection;

import chess.Move;
import chess.Square;

public class MoveCacheBoard {
	
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
		long keyAdded = key.getPosicion();
		for(int i = 0; i < 64; i++){
			if( (affectedByCollection & (1L << i))  != 0 ) {
				 affects[i] |= keyAdded;
			}
		}
	}

	public void clearPseudoMoves(Square key) {
		clearPseudoMoves(affects[key.toIdx()] | key.getPosicion());
	}

	public void clearPseudoMoves(Square key1, Square key2) {
		clearPseudoMoves(affects[key1.toIdx()] | key1.getPosicion() | affects[key2.toIdx()] | key2.getPosicion());
	}
	
	public void clearPseudoMoves(Square key1, Square key2, Square key3) {
		clearPseudoMoves(affects[key1.toIdx()] | key1.getPosicion() | affects[key2.toIdx()] | key2.getPosicion() | affects[key3.toIdx()] | key3.getPosicion());
	}	
	
	private void clearPseudoMoves(long clearSquares) {
		long affectsBySquares = 0;
		for(int i = 0; i < 64; i++){
			if( (clearSquares & (1L << i))  != 0 ) {
				affectsBySquares |= affectedBy[i];
				affectedBy[i] = 0;
				pseudoMoves[i] = null;
			}
		}
		
		long keyRemoved = ~clearSquares;
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= keyRemoved;
			}
		}		

	}

	public void validar() {
		
		//Validate affectedBy[]
		for(int i = 0; i < 64; i++){
			long affectedBySquares = affectedBy[i];
			if(affectedBySquares != 0) {
				if(pseudoMoves[i] == null) {
					throw new RuntimeException("MoveCacheBoard checkConsistence failed, there are not pseudoMoves[i] ");
				}				
				for(int j = 0; j < 64; j++){
					if( (affectedBySquares & (1L << j))  != 0 ) {
						if((affects[j] & (1L << i)) == 0){
							throw new RuntimeException("MoveCacheBoard checkConsistence failed");
						}
					}
				}
			} else {
				if(pseudoMoves[i] != null) {
					throw new RuntimeException("MoveCacheBoard checkConsistence failed, there are pseudoMoves[i] ");
				}
			}
		}
		
		//Validate affects[]
		for(int i = 0; i < 64; i++){
			long affectsSquares = affects[i];
			for(int j = 0; j < 64; j++){
				if( (affectsSquares & (1L << j))  != 0 ) {
					if((affectedBy[j] & (1L << i)) == 0){
						throw new RuntimeException("MoveCacheBoard checkConsistence failed");
					}
				}
			}
		}		
		
	}	

}
