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

public class PiecePlacementBuilder implements ChessRepresentationBuilder<SquareBoard> {
	
	private final SquareBoard squareBoard;
	
	public PiecePlacementBuilder() {
		this(new ChessFactory());
	}
	
	public PiecePlacementBuilder(ChessFactory chessFactory) {
		this.squareBoard = chessFactory.createPiecePlacement();
	}	
	
	@Override
	public SquareBoard getChessRepresentation() {
		return squareBoard;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withTurn(Color turn) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<SquareBoard> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<SquareBoard> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<SquareBoard> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	public ChessRepresentationBuilder<SquareBoard> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}


}
