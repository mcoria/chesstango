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

		for(int i = 0; i < 64; i++){
			if(pseudoMoves[i] != null && dummyBoard.isEmpty(Square.getSquareByIdx(i))) {
				throw new RuntimeException(String.format("Un casillero de cache contiene movimientos (%s) pero no existe pieza en tablero!!!", Square.getSquareByIdx(i)));
			}
		}

		for (PiecePositioned piecePositioned : dummyBoard) {
			if(piecePositioned.getPiece() == null && pseudoMoves[piecePositioned.getSquare().toIdx()] != null){
				throw new RuntimeException(String.format("Para un casillero de tablero sin pieza (%s) existe movimientos en cache!!!", piecePositioned.getSquare()) );
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
