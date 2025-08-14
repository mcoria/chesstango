package net.chesstango.board.internal.position;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.position.KingSquare;
import net.chesstango.board.position.SquareBoardReader;

/**
 * @author Mauricio Coria
 *
 */
public class KingSquareImp implements KingSquare {
	
	protected Square squareKingWhiteCache = null;
	
	protected Square squareKingBlackCache = null;
	
	@Override
	public void init(SquareBoardReader board){
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
		return Color.WHITE.equals(color) ? getKingSquareWhite() : getKingSquareBlack();
	}

	@Override
	public Square getKingSquareWhite() {
		return squareKingWhiteCache;
	}
	
	@Override
	public Square getKingSquareBlack() {
		return squareKingBlackCache;
	}

	///////////////////////////// END getKingSquare Logic /////////////////////////////
	
	@Override
	public KingSquareImp clone() throws CloneNotSupportedException {
		KingSquareImp clone = new KingSquareImp();
		clone.squareKingWhiteCache = this.squareKingWhiteCache;
		clone.squareKingBlackCache = this.squareKingBlackCache;
		return clone;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof KingSquareImp theInstance){
			return this.squareKingWhiteCache.equals(theInstance.squareKingWhiteCache) && this.squareKingBlackCache.equals(theInstance.squareKingBlackCache);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "King White: " + (squareKingWhiteCache == null ? "-" : squareKingWhiteCache.toString())
				+ ", King Black: " + (squareKingBlackCache == null ? "-" : squareKingBlackCache.toString());
	}


	public Square getKingSquare(Color color, SquareBoardReader board) {
		Square kingSquare = null;
		Piece king = Piece.getKing(color);
		for (PiecePositioned entry : board) {
			Square currentSquare = entry.square();
			Piece currentPieza = entry.piece();
			if (king.equals(currentPieza)) {
				kingSquare = currentSquare;
				break;
			}
		}
		return kingSquare;
	}
}


