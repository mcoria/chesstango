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
		if (squareKingWhiteCache != null && !Pieza.KING_WHITE.equals(dummyBoard.getPieza(squareKingWhiteCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedó desactualizado");
		}

		if (squareKingBlackCache != null && !Pieza.KING_BLACK.equals(dummyBoard.getPieza(squareKingBlackCache))) {
			throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedó desactualizado");
		}
		
		for (PosicionPieza pieza : dummyBoard) {
			if (Pieza.KING_WHITE.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingWhiteCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingWhiteCache quedó desactualizado");
			}
			if (Pieza.KING_BLACK.equals(pieza.getValue()) && !pieza.getKey().equals(squareKingBlackCache)) {
				throw new RuntimeException("KingCacheBoard - squareKingBlackCache quedó desactualizado");
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
