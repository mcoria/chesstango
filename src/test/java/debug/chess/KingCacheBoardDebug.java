package debug.chess;

import chess.Pieza;
import layers.KingCacheBoard;
import layers.PosicionPiezaBoard;

public class KingCacheBoardDebug extends KingCacheBoard {

	public KingCacheBoardDebug(PosicionPiezaBoard posicionPiezaBoard) {
		super(posicionPiezaBoard);
	}
	
	public void validar(PosicionPiezaBoard dummyBoard) {
		if (!Pieza.REY_BLANCO.equals(dummyBoard.getPieza(squareKingBlancoCache))) {
			throw new RuntimeException("KingCacheBoard - rey blanco quedó desactualizado");
		}

		if (!Pieza.REY_NEGRO.equals(dummyBoard.getPieza(squareKingNegroCache))) {
			throw new RuntimeException("KingCacheBoard - rey negro quedó desactualizado");
		}
	}	

}
