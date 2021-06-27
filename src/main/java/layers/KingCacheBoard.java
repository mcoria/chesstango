package layers;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

public class KingCacheBoard {
	
	private Square squareKingBlancoCache = null;
	
	private Square squareKingNegroCache = null;
	
	public KingCacheBoard(DummyBoard board) {
		this.squareKingBlancoCache = getKingSquareRecorrer(Color.BLANCO, board);
		this.squareKingNegroCache = getKingSquareRecorrer(Color.NEGRO, board);		
	}
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	
	public void setKingSquare(Color color, Square square) {
		if(Color.BLANCO.equals(color)){
			this.squareKingBlancoCache = square;
		} else {
			this.squareKingNegroCache = square;
		}
	}
	
	public Square getKingSquare(Color color) {
		return Color.BLANCO.equals(color) ? getSquareKingBlancoCache() : getSquareKingNegroCache();
	}	
	
	public Square getSquareKingBlancoCache() {
		return squareKingBlancoCache;
	}
	
	public Square getSquareKingNegroCache() {
		return squareKingNegroCache;
	}
	
	private Square getKingSquareRecorrer(Color color, DummyBoard board) {
		Square kingSquare = null;
		Pieza rey = Pieza.getRey(color);
		for (PosicionPieza entry : board) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(rey.equals(currentPieza)){
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}
	///////////////////////////// END getKingSquare Logic /////////////////////////////		
	
}


