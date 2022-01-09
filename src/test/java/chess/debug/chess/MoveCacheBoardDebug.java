package chess.debug.chess;

import chess.PiecePositioned;
import chess.Square;
import chess.position.MoveCacheBoard;
import chess.position.PiecePlacement;
import chess.pseudomovesgenerators.MoveGeneratorStrategy;


/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardDebug extends MoveCacheBoard {

	/**
	 * @param piecePlacement
	 * @param strategy
	 */
	public MoveCacheBoardDebug(PiecePlacement piecePlacement, MoveGeneratorStrategy strategy) {
		super(piecePlacement, strategy);
	}

	public void validar(PiecePlacement dummyBoard) {
		validar();
		
		for (PiecePositioned piecePositioned : dummyBoard) {
			if(piecePositioned.getValue() == null && pseudoMoves[piecePositioned.getKey().toIdx()] != null){
				throw new RuntimeException("En un casillero vacio de tablero existe movimeintos de cache!!!");
			}
		}
		
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null && dummyBoard.isEmtpy(Square.getSquare(i))) {
				throw new RuntimeException("En un casillero de cache con movimeintos se encuentra vacio en el tablero!!!");
			}
		}
	}	
	
	public void validar() {
		//Validate affectedBy[]
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null) {
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
				if( (affectsSquares & (1L << j)) != 0 ) {
					if((pseudoMoves[j].getAffectedBy() & (1L << i)) == 0){
						throw new RuntimeException("MoveCacheBoard checkConsistence failed");
					}
				}
			}
		}
	}

}
