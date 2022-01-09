package chess.position;

import chess.Color;
import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoard {
	
	protected Square squareKingWhiteCache = null;
	
	protected Square squareKingBlackCache = null;
	
	public KingCacheBoard(PiecePlacement board) {
		this.squareKingWhiteCache = getKingSquareRecorrer(Color.WHITE, board);
		this.squareKingBlackCache = getKingSquareRecorrer(Color.BLACK, board);		
	}
	
	public KingCacheBoard(){
	}
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	
	public void setKingSquare(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			this.squareKingWhiteCache = square;
		} else {
			this.squareKingBlackCache = square;
		}
	}
	
	public Square getKingSquare(Color color) {
		return Color.WHITE.equals(color) ? getSquareKingWhiteCache() : getSquareKingBlackCache();
	}	
	
	public Square getSquareKingWhiteCache() {
		return squareKingWhiteCache;
	}
	
	public Square getSquareKingBlackCache() {
		return squareKingBlackCache;
	}
	
	private Square getKingSquareRecorrer(Color color, PiecePlacement board) {
		Square kingSquare = null;
		Piece king = Piece.getKing(color);
		for (PiecePositioned entry : board) {
			Square currentSquare = entry.getKey();
			Piece currentPieza = entry.getValue();
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
		clone.squareKingWhiteCache = this.squareKingWhiteCache;
		clone.squareKingBlackCache = this.squareKingBlackCache;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KingCacheBoard){
			KingCacheBoard theInstance = (KingCacheBoard) obj;
			return this.squareKingWhiteCache.equals(theInstance.squareKingWhiteCache) && this.squareKingBlackCache.equals(theInstance.squareKingBlackCache); 
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "King Blanco: " + squareKingWhiteCache.toString() + ", King Negro: " + squareKingBlackCache.toString();
	}
	
}

