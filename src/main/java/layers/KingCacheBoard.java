package layers;

import chess.Color;
import chess.Pieza;
import chess.PosicionPieza;
import chess.Square;

public class KingCacheBoard {
	
	private Square squareKingBlancoCache = null;
	
	private Square squareKingNegroCache = null;
	
	public KingCacheBoard(PosicionPiezaBoard board) {
		this.squareKingBlancoCache = getKingSquareRecorrer(Color.BLANCO, board);
		this.squareKingNegroCache = getKingSquareRecorrer(Color.NEGRO, board);		
	}
	
	public KingCacheBoard(){
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
	
	private Square getKingSquareRecorrer(Color color, PosicionPiezaBoard board) {
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
		// TODO Auto-generated method stub
		return "Rey Blanco: " + squareKingBlancoCache.toString() + ", Rey Negro: " + squareKingNegroCache.toString();
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


