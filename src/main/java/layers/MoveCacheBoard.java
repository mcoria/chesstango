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
		long keyRemoved = ~key.getPosicion();
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= keyRemoved;
			}
		}
		affectedBy[key.toIdx()] = 0;		
		
		//Current square pseudomove
		pseudoMoves[key.toIdx()] = null;
	}
	
	public void clearPseudoMoves(Square key1, Square key2) {
		// Affects squares
		long affectsSquares = affects[key1.toIdx()] | affects[key2.toIdx()] ;
		for(int i = 0; i < 64; i++){
			if( (affectsSquares & (1L << i))  != 0 ) {
				pseudoMoves[i] = null;
			}
		}
		affects[key1.toIdx()] = 0;
		affects[key2.toIdx()] = 0;
		
		// AffectedBy squares
		long affectsBySquares = affectedBy[key1.toIdx()] | affectedBy[key2.toIdx()];
		long keyRemoved = ~ (key1.getPosicion() | key2.getPosicion());
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= keyRemoved ;
			}
		}
		affectedBy[key1.toIdx()] = 0;
		affectedBy[key2.toIdx()] = 0;
		
		//Current square pseudomove
		pseudoMoves[key1.toIdx()] = null;
		pseudoMoves[key2.toIdx()] = null;
	}
	
	public void clearPseudoMoves(Square key1, Square key2, Square key3) {
		// Affects squares
		long affectsSquares = affects[key1.toIdx()] | affects[key2.toIdx()] | affects[key3.toIdx()] ;
		for(int i = 0; i < 64; i++){
			if( (affectsSquares & (1L << i))  != 0 ) {
				pseudoMoves[i] = null;
			}
		}
		affects[key1.toIdx()] = 0;
		affects[key2.toIdx()] = 0;
		affects[key3.toIdx()] = 0;
		
		// AffectedBy squares
		long affectsBySquares = affectedBy[key1.toIdx()] | affectedBy[key2.toIdx()] | affectedBy[key3.toIdx()];
		long keyRemoved = ~ (key1.getPosicion() | key2.getPosicion() | key3.getPosicion());
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= keyRemoved ;
			}
		}
		affectedBy[key1.toIdx()] = 0;
		affectedBy[key2.toIdx()] = 0;
		affectedBy[key3.toIdx()] = 0;
		
		//Current square pseudomove
		pseudoMoves[key1.toIdx()] = null;
		pseudoMoves[key2.toIdx()] = null;
		pseudoMoves[key3.toIdx()] = null;
	}		

}
