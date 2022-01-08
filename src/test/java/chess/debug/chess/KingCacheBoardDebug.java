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
		if (squareKingBlancoCache != null && !Pieza.KING_WHITE.equals(dummyBoard.getPieza(squareKingBlancoCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlancoCache quedó desactualizado");
		}

		if (squareKingNegroCache != null && !Pieza.KING_BLACK.equals(dummyBoard.getPieza(squareKingNegroCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingNegroCache quedó desactualizado");
		}
		
		for (PosicionPieza pieza : dummyBoard) {
			if (Pieza.KING_WHITE.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlancoCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlancoCache quedó desactualizado");
			}
			if (Pieza.KING_BLACK.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingNegroCache)) {
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
