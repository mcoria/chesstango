package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.position.SquareBoard;


/**
 * @author Mauricio Coria
 *
 */

public class SquareBoardBuilder implements ChessPositionBuilder<SquareBoard> {
	
	private final SquareBoard squareBoard;
	
	public SquareBoardBuilder() {
		this(new ChessFactory());
	}
	
	public SquareBoardBuilder(ChessFactory chessFactory) {
		this.squareBoard = chessFactory.createPiecePlacement();
	}	
	
	@Override
	public SquareBoard getChessRepresentation() {
		return squareBoard;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withTurn(Color turn) {
		return this;
	}


	@Override
	public ChessPositionBuilder<SquareBoard> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}


	@Override
	public ChessPositionBuilder<SquareBoard> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public ChessPositionBuilder<SquareBoard> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	public ChessPositionBuilder<SquareBoard> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}


}
