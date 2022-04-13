package chess.board.position.imp;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import chess.board.Square;
import chess.board.pseudomovesgenerators.MoveGeneratorResult;

/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoard {
	
	protected MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
	protected long affects[] = new long[64];

	private List<MoveGeneratorResult> currentClearedSquares = new ArrayList<MoveGeneratorResult>();
	private Deque<List<MoveGeneratorResult>> clearedSquares = new ArrayDeque<List<MoveGeneratorResult>>();
	
	public MoveGeneratorResult getPseudoMovesResult(Square key) {
		return  pseudoMoves[key.toIdx()];
	}	
	 
	public void setPseudoMoves(Square key, MoveGeneratorResult generatorResult) {
		pseudoMoves[key.toIdx()] = generatorResult;
		long keyAdded = key.getPosicion();
		long affectedByCollection = generatorResult.getAffectedBy();
		for(int i = 0; i < 64; i++){
			if( (affectedByCollection & (1L << i))  != 0 ) {
				 affects[i] |= keyAdded;
			}
		}
	}		

	public void clearPseudoMoves(Square key, boolean trackCleared) {
		clearPseudoMoves(affects[key.toIdx()] | (pseudoMoves[key.toIdx()] != null ? key.getPosicion() : 0), trackCleared);
	}

	public void clearPseudoMoves(Square key1, Square key2, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0), trackCleared);
	}

	public void clearPseudoMoves(Square key1, Square key2, Square key3, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0)
				| affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getPosicion() : 0), trackCleared);
	}

	public void clearPseudoMoves(Square key1, Square key2, Square key3, Square key4, boolean trackCleared) {
		clearPseudoMoves(affects[key1.toIdx()] | (pseudoMoves[key1.toIdx()] != null ? key1.getPosicion() : 0)
				| affects[key2.toIdx()] | (pseudoMoves[key2.toIdx()] != null ? key2.getPosicion() : 0)
				| affects[key3.toIdx()] | (pseudoMoves[key3.toIdx()] != null ? key3.getPosicion() : 0)
				| affects[key4.toIdx()] | (pseudoMoves[key4.toIdx()] != null ? key4.getPosicion() : 0), trackCleared);
	}	
	
	private void clearPseudoMoves(long clearSquares, boolean trackCleared) {
		long affectsBySquares = 0;
		for(int i = 0; i < 64; i++){
			if( (clearSquares & (1L << i))  != 0 ) {
				MoveGeneratorResult pseudoMove = pseudoMoves[i];
				if(pseudoMove != null){
					affectsBySquares |= pseudoMove.getAffectedBy();
					if(trackCleared){
						currentClearedSquares.add(pseudoMove);
					}
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

	
	public void pushCleared() {
		clearedSquares.push(currentClearedSquares);
		currentClearedSquares = new ArrayList<MoveGeneratorResult>();
	}

	//TODO: este metodo consume el 20% del procesamiento
	public void popCleared() {
		for(MoveGeneratorResult generatorResult: currentClearedSquares){
			if(generatorResult != null){
				setPseudoMoves(generatorResult.getFrom().getKey(), generatorResult);
			}
		}
		currentClearedSquares = clearedSquares.pop();
	}	

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer(); 
		for (MoveGeneratorResult result : pseudoMoves) {
			if(result != null){
				buffer.append(result.toString() + "\n");
			}
		}
		
		return buffer.toString();
	}
}