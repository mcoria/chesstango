package debug.chess;

import layers.MoveCacheBoard;

public class MoveCacheBoardDebug extends MoveCacheBoard {

	//TODO: deberiamos extraer este metodo validar y llevarlo a una clase derivada
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
