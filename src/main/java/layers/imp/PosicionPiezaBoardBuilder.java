package layers.imp;

import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;

public class PosicionPiezaBoardBuilder{
	
	public <T extends PosicionPiezaBoard> PosicionPiezaBoard buildPosicionPiezaBoard(Class<T> theClass, Pieza[][] tablero){
		try {
			T instance = theClass.newInstance();
			for (int file = 0; file < 8; file++) {
				for (int rank = 0; rank < 8; rank++) {
					Square square = Square.getSquare(file, rank);
					Pieza pieza = tablero[file][rank];
					if(pieza == null){
						instance.setEmptySquare(square);
					} else {
						instance.setPieza(square, pieza);
					}
				}
			}			
			return instance;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	//This is default implementation
	public PosicionPiezaBoard buildPosicionPiezaBoard(Pieza[][] tablero){
		return buildPosicionPiezaBoard(ArrayPosicionPiezaBoard.class, tablero);
	}	

}
