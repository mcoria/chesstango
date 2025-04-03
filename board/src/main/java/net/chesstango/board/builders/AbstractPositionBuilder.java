/**
 * 
 */
package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractPositionBuilder<T> implements PositionBuilder<T> {
	protected Color turn;
	protected Square enPassantSquare;
	protected boolean castlingBlackKingAllowed;
	protected boolean castlingBlackQueenAllowed;
	protected boolean castlingWhiteKingAllowed;
	protected boolean castlingWhiteQueenAllowed;
	protected int halfMoveClock;
	protected int fullMoveClock;

	protected Piece[][] board = new Piece[8][8];

	@Override
	public PositionBuilder<T> withPiece(Square square, Piece piece) {
		this.board[square.getRank()][square.getFile()] = piece;
		return this;
	}

	@Override
	public PositionBuilder<T> withTurn(Color turn) {
		this.turn = turn;
		return this;
	}

	@Override
	public PositionBuilder<T> withEnPassantSquare(Square enPassantSquare) {
		this.enPassantSquare = enPassantSquare;
		return this;
	}

	@Override
	public PositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		this.castlingBlackKingAllowed = castlingBlackKingAllowed;
		return this;
	}

	@Override
	public PositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
		return this;
	}

	@Override
	public PositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
		return this;
	}

	@Override
	public PositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
		return this;
	}

	@Override
	public PositionBuilder<T> withHalfMoveClock(int halfMoveClock){
		this.halfMoveClock = halfMoveClock;
		return this;
	}

	@Override
	public PositionBuilder<T> withFullMoveClock(int fullMoveClock){
		this.fullMoveClock = fullMoveClock;
		return this;
	}

}
