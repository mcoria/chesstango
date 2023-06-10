package net.chesstango.board.position.imp;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.BoardReader;
import net.chesstango.board.position.KingCacheBoard;

/**
 * @author Mauricio Coria
 *
 */
public class KingCacheBoardImp implements KingCacheBoard {
	
	protected Square squareKingWhiteCache = null;
	
	protected Square squareKingBlackCache = null;
	
	@Override
	public void init(BoardReader board){
		this.squareKingWhiteCache = getKingSquare(Color.WHITE, board);
		this.squareKingBlackCache = getKingSquare(Color.BLACK, board);
	}	
	
	///////////////////////////// START getKingSquare Logic /////////////////////////////
	
	@Override
	public void setKingSquare(Color color, Square square) {
		if(Color.WHITE.equals(color)){
			this.squareKingWhiteCache = square;
		} else {
			this.squareKingBlackCache = square;
		}
	}
	
	@Override
	public Square getKingSquare(Color color) {
		return Color.WHITE.equals(color) ? getSquareKingWhiteCache() : getSquareKingBlackCache();
	}

	@Override
	public Square getSquareKingWhiteCache() {
		return squareKingWhiteCache;
	}
	
	@Override
	public Square getSquareKingBlackCache() {
		return squareKingBlackCache;
	}

	///////////////////////////// END getKingSquare Logic /////////////////////////////
	
	@Override
	public KingCacheBoardImp clone() throws CloneNotSupportedException {
		KingCacheBoardImp clone = new KingCacheBoardImp();
		clone.squareKingWhiteCache = this.squareKingWhiteCache;
		clone.squareKingBlackCache = this.squareKingBlackCache;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KingCacheBoardImp){
			KingCacheBoardImp theInstance = (KingCacheBoardImp) obj;
			return this.squareKingWhiteCache.equals(theInstance.squareKingWhiteCache) && this.squareKingBlackCache.equals(theInstance.squareKingBlackCache); 
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "King White: " + (squareKingWhiteCache == null ? "-" : squareKingWhiteCache.toString())
				+ ", King Black: " + (squareKingBlackCache == null ? "-" : squareKingBlackCache.toString());
	}


	public Square getKingSquare(Color color, BoardReader board) {
		Square kingSquare = null;
		Piece king = Piece.getKing(color);
		for (PiecePositioned entry : board) {
			Square currentSquare = entry.getSquare();
			Piece currentPieza = entry.getPiece();
			if (king.equals(currentPieza)) {
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}
}


