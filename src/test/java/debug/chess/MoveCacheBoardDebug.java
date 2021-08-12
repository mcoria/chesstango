package debug.chess;

import chess.PosicionPieza;
import chess.Square;
import layers.MoveCacheBoard;
import layers.PosicionPiezaBoard;

public class MoveCacheBoardDebug extends MoveCacheBoard {

	public void validar(PosicionPiezaBoard dummyBoard) {
		validar();
		
		for (PosicionPieza posicionPieza : dummyBoard) {
			if(posicionPieza.getValue() == null && pseudoMoves[posicionPieza.getKey().toIdx()] != null){
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
