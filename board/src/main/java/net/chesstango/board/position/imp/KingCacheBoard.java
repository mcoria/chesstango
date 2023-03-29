package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.BoardReader;

/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoard {
	
	protected Square squareKingWhiteCache = null;
	
	protected Square squareKingBlackCache = null;
	
	public void init(BoardReader board){
		this.squareKingWhiteCache = getKingSquareRecorrer(Color.WHITE, board);
		this.squareKingBlackCache = getKingSquareRecorrer(Color.BLACK, board);			
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
	
	private Square getKingSquareRecorrer(Color color, BoardReader board) {
		Square kingSquare = null;
		Piece king = Piece.getKing(color);
		for (PiecePositioned entry : board) {
			Square currentSquare = entry.getSquare();
			Piece currentPieza = entry.getPiece();
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
		return "King White: " + (squareKingWhiteCache == null ? "-" : squareKingWhiteCache.toString())
				+ ", King Black: " + (squareKingBlackCache == null ? "-" : squareKingBlackCache.toString());
	}
	
}


