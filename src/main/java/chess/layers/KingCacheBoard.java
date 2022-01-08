package chess.layers;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoard {
	
	protected Square squareKingBlancoCache = null;
	
	protected Square squareKingNegroCache = null;
	
	public KingCacheBoard(PosicionPiezaBoard board) {
		this.squareKingBlancoCache = getKingSquareRecorrer(Color.WHITE, board);
		this.squareKingNegroCache = getKingSquareRecorrer(Color.BLACK, board);		
	}
	
	public KingCacheBoard(){
	}
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	
	public void setKingSquare(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			this.squareKingBlancoCache = square;
		} else {
			this.squareKingNegroCache = square;
		}
	}
	
	public Square getKingSquare(Color color) {
		return Color.WHITE.equals(color) ? getSquareKingBlancoCache() : getSquareKingNegroCache();
	}	
	
	public Square getSquareKingBlancoCache() {
		return squareKingBlancoCache;
	}
	
	public Square getSquareKingNegroCache() {
		return squareKingNegroCache;
	}
	
	private Square getKingSquareRecorrer(Color color, PosicionPiezaBoard board) {
		Square kingSquare = null;
		Pieza king = Pieza.getKing(color);
		for (PosicionPieza entry : board) {
			Square currentSquare = entry.getKey();
			Pieza currentPieza = entry.getValue();
			if(king.equals(currentPieza)){
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}
	///////////////////////////// END getKingSquare Logic /////////////////////////////
	
	@Override
	public KingCacheBoard clone() throws CloneNotSupportedException {
		KingCacheBoard clone = new KingCacheBoard();
		clone.squareKingBlancoCache = this.squareKingBlancoCache;
		clone.squareKingNegroCache = this.squareKingNegroCache;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KingCacheBoard){
			KingCacheBoard theInstance = (KingCacheBoard) obj;
			return this.squareKingBlancoCache.equals(theInstance.squareKingBlancoCache) && this.squareKingNegroCache.equals(theInstance.squareKingNegroCache); 
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "King Blanco: " + squareKingBlancoCache.toString() + ", King Negro: " + squareKingNegroCache.toString();
	}
	
}


