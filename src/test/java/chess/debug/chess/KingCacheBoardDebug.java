package chess.debug.chess;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;
import chess.layers.KingCacheBoard;
import chess.layers.PosicionPiezaBoard;


/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoardDebug extends KingCacheBoard {

	public KingCacheBoardDebug(PosicionPiezaBoard posicionPiezaBoard) {
		super(posicionPiezaBoard);
	}
	
	public void validar(PosicionPiezaBoard dummyBoard) {
		if (squareKingBlancoCache != null && !Pieza.REY_BLANCO.equals(dummyBoard.getPieza(squareKingBlancoCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlancoCache quedó desactualizado");
		}

		if (squareKingNegroCache != null && !Pieza.REY_NEGRO.equals(dummyBoard.getPieza(squareKingNegroCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingNegroCache quedó desactualizado");
		}
		
		for (PosicionPieza pieza : dummyBoard) {
			if (Pieza.REY_BLANCO.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlancoCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlancoCache quedó desactualizado");
			}
			if (Pieza.REY_NEGRO.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingNegroCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingNegroCache quedó desactualizado");
			}			
		}
		
	}
	
	@Override
	public Square getKingSquare(Color color) {
		if(color == null){
			throw new RuntimeException("getKingSquare() - invoked with null parameter");
		}
		return super.getKingSquare(color);
	}

}
