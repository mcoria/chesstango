package layers;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;

import chess.PosicionPieza;
import chess.Square;
import moveexecutors.Move;
import movegenerators.MoveGenerator;
import movegenerators.MoveGeneratorResult;
import movegenerators.MoveGeneratorStrategy;

/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoard {
	
	protected MoveGeneratorResult[] pseudoMoves = new MoveGeneratorResult[64];
	protected long affects[] = new long[64];

	private MoveGeneratorResult[] currentClearedSquares = new MoveGeneratorResult[64];
	private Deque<MoveGeneratorResult[]> clearedSquares = new ArrayDeque<MoveGeneratorResult[]>();
	

	public MoveCacheBoard() {
	}
	
	/**
	 * @param posicionPiezaBoard
	 * @param buildMoveGeneratorStrategy
	 */
	public MoveCacheBoard(PosicionPiezaBoard posicionPiezaBoard, MoveGeneratorStrategy strategy) {
		for(PosicionPieza origen: posicionPiezaBoard){
			
			if(origen.getValue() != null){
				MoveGenerator moveGenerator =  strategy.getMoveGenerator(origen.getValue());
												//origen.getValue().getMoveGenerator(strategy); Mala performance
	
				MoveGeneratorResult generatorResult = moveGenerator.calculatePseudoMoves(origen);
	
				setPseudoMoves(origen.getKey(), generatorResult);
			}
		}
	}

	public Collection<Move> getPseudoMoves(Square key) {
		MoveGeneratorResult result = pseudoMoves[key.toIdx()];
		return result == null ? null  : result.getPseudoMoves();
	}
	
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
						currentClearedSquares[i] = pseudoMoves[i];
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
		currentClearedSquares = new MoveGeneratorResult[64];
	}

	public void popCleared() {
		for(int i = 0; i < 64; i++){
			MoveGeneratorResult generatorResult = currentClearedSquares[i];
			if(generatorResult != null){
				setPseudoMoves(Square.getSquare(i), generatorResult);
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
