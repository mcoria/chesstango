package net.chesstango.board.debug.chess;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.MoveCacheBoard;


/**
 * @author Mauricio Coria
 *
 */
public class MoveCacheBoardDebug extends MoveCacheBoard {

	/**
	 * @param piecePlacement
	 * @param strategy
	 */
	public MoveCacheBoardDebug() {
		super();
	}

	public void validar(PiecePlacement dummyBoard) {
		validarAffectedByAndAffects();
		
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

	private void validarAffectedByAndAffects() {
		//Validate affectedBy[]
		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null) {
				long affectedBySquares = pseudoMoves[i].getAffectedByPositions();
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
					if((pseudoMoves[j].getAffectedByPositions() & (1L << i)) == 0){
						throw new RuntimeException("MoveCacheBoard checkConsistence failed");
					}
				}
			}
		}
	}	

}
