package layers;

import java.util.Collection;

import chess.Square;
import moveexecutors.Move;
import movegenerators.MoveGeneratorResult;

public class MoveCacheBoard {
	
	protected MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
	protected long affects[] = new long[64];
	
	public Collection<Move> getPseudoMoves(Square key) {
		MoveGeneratorResult result = pseudoMoves[key.toIdx()];
		return result == null ? null  : result.getPseudoMoves();
	}
	
	public MoveGeneratorResult getPseudoMovesResult(Square key) {
		return  pseudoMoves[key.toIdx()];
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
		clearPseudoMoves(affects[key.toIdx()] | (pseudoMoves[key.toIdx()] != null ? key.getPosicion() : 0));
	}

	public void clearPseudoMoves(Square key1, Square key2) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0));
	}

	public void clearPseudoMoves(Square key1, Square key2, Square key3) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0)
				| affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getPosicion() : 0));
	}

	public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0)
				| affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getPosicion() : 0)
				| affects[key4.toIdx()] | (pseudoMoves[key4.toIdx()] != null ? key4.getPosicion() : 0));
	}	
	
	private void clearPseudoMoves(long clearSquares) {
		long affectsBySquares = 0;
		for(int i = 0; i < 64; i++){
			if( (clearSquares & (1L << i))  != 0 ) {
				MoveGeneratorResult pseudoMove = pseudoMoves[i];
				if(pseudoMove != null){
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


}
