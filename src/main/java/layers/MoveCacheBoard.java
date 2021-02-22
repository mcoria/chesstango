package layers;

import java.util.Collection;

import chess.Move;
import chess.Square;
import movegenerators.MoveGeneratorResult;

public class MoveCacheBoard {
	
	private MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
	private long affects[] = new long[64];
	
	/*
	private static class MoveCacheBoardNode{
		@SuppressWarnings("unchecked")
		private Collection<Move> pseudoMoves[] = new Collection[64];
		private long affects[] = new long[64];
		private long affectedBy[] = new long[64];		
	}*/
	
	//private Deque<MoveCacheBoardNode> moveCacheBoardPila = new ArrayDeque<MoveCacheBoardNode>();	
	
	public Collection<Move> getPseudoMoves(Square key) {
		MoveGeneratorResult result = pseudoMoves[key.toIdx()];
		return result == null ? null  : result.getPseudoMoves();
	}
	
	public void setPseudoMoves(Square key, MoveGeneratorResult generatorResult) {
		if(generatorResult.isSaveMovesInCache()){
			pseudoMoves[key.toIdx()] = generatorResult;
			long keyAdded = key.getPosicion();
			long affectedByCollection = generatorResult.getAffectedBy();
			for(int i = 0; i < 64; i++){
				if( (affectedByCollection & (1L << i))  != 0 ) {
					 affects[i] |= keyAdded;
				}
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
	
	public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4) {
		clearPseudoMoves(affects[key1.toIdx()] | key1.getPosicion() | affects[key2.toIdx()] | key2.getPosicion() | affects[key3.toIdx()] | key3.getPosicion() | affects[key4.toIdx()] | key4.getPosicion());
	}	
	
	private void clearPseudoMoves(long clearSquares) {
		long affectsBySquares = 0;
		for(int i = 0; i < 64; i++){
			if( (clearSquares & (1L << i))  != 0 ) {
				MoveGeneratorResult pseudoMove = pseudoMoves[i];
				if(pseudoMoves[i] != null){
					affectsBySquares |= pseudoMove.getAffectedBy();
					pseudoMoves[i] = null;
				}
			}
		}
		
		long keyRemoved = ~clearSquares;
		for(int i = 0; i < 64; i++){
			if( (affectsBySquares & (1L << i))  != 0 ) {
				affects[i] &= keyRemoved;
			}
		}		

	}
	
	
	
/*
	public void pushState() {
		MoveCacheBoardNode state = saveState();
		
		moveCacheBoardPila.push( state );
	}

	public void popState() {
		MoveCacheBoardNode lastState = moveCacheBoardPila.pop();
		
		restoreState(lastState);
	}

	private MoveCacheBoardNode saveState() {
		MoveCacheBoardNode node = new MoveCacheBoardNode();
		for(int i = 0; i < 64; i++){
			node.affectedBy[i] = affectedBy[i];
			node.affects[i] = affects[i];
			node.pseudoMoves[i] = pseudoMoves[i];
		}
		return node;
	}
	
	private void restoreState(MoveCacheBoardNode lastState){
		for(int i = 0; i < 64; i++){
			affectedBy[i] = lastState.affectedBy[i];
			affects[i] = lastState.affects[i];
			pseudoMoves[i] = lastState.pseudoMoves[i];
		}
	}
	*/

	public void validar() {
		
		//Validate affectedBy[]
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null) {
				if(pseudoMoves[i] == null) {
					throw new RuntimeException("MoveCacheBoard checkConsistence failed, there are not pseudoMoves[i] ");
				}
				long affectedBySquares = pseudoMoves[i].getAffectedBy();
				for(int j = 0; j < 64; j++){
					if( (affectedBySquares & (1L << j))  != 0 ) {
						if((affects[j] & (1L << i)) == 0){
							throw new RuntimeException("MoveCacheBoard checkConsistence failed");
						}
					}
				}
			}
		}
		
		//Validate affects[]
		for(int i = 0; i < 64; i++){
			long affectsSquares = affects[i];
			for(int j = 0; j < 64; j++){
				if( (affectsSquares & (1L << j))  != 0 ) {
					if((pseudoMoves[j].getAffectedBy() & (1L << i)) == 0){
						throw new RuntimeException("MoveCacheBoard checkConsistence failed");
					}
				}
			}
		}		
		
	}


}
