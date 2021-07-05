package debug.chess;

import chess.Color;
import chess.PosicionPieza;
import layers.ColorBoard;
import layers.PosicionPiezaBoard;

public class ColorBoardDebug extends ColorBoard {

	public ColorBoardDebug(PosicionPiezaBoard board) {
		super(board);
	}
		
	
	//TODO: deberiamos extraer este metodo validar y llevarlo a una clase derivada
	public void validar(PosicionPiezaBoard board) {
		int posicionesBlancas = 0;
		int posicionesNegras = 0;
		for (PosicionPieza posicionPieza : board) {
			if(posicionPieza.getValue() != null){
				Color color = posicionPieza.getValue().getColor();
				if(Color.BLANCO.equals(color)){
					posicionesBlancas++;
					if( (squareBlancos & posicionPieza.getKey().getPosicion()) == 0   ){
						throw new RuntimeException("La posicion squareBlancos no se encuentra");
					}
				} else {
					posicionesNegras++;
					if( (squareNegros & posicionPieza.getKey().getPosicion()) == 0){
						throw new RuntimeException("La posicion squareNegros no se encuentra");
					}					
				}
			}
		}
		
		if( Long.bitCount(squareBlancos) != posicionesBlancas ){
			throw new RuntimeException("Diferencias en cantidad de posicions blancas");
		}
		
		if( Long.bitCount(squareNegros) != posicionesNegras ){
			throw new RuntimeException("Diferencias en cantidad de posicions negras");
		}
	}	
}
