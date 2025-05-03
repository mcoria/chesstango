package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.internal.position.SquareBoardImp;
import net.chesstango.board.representations.PositionBuilder;

/**
 * @author Mauricio Coria
 *
 */
public class SquareBoardBuilder implements PositionBuilder<SquareBoard> {
	
	private final SquareBoard squareBoard;
	
	public SquareBoardBuilder() {
		this(new SquareBoardImp());
	}
	
	SquareBoardBuilder(SquareBoard squareBoard) {
		this.squareBoard = squareBoard;
	}	
	
	@Override
	public SquareBoard getPositionRepresentation() {
		return squareBoard;
	}

	@Override
	public PositionBuilder<SquareBoard> withTurn(Color turn) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	@Override
	public PositionBuilder<SquareBoard> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}
}
