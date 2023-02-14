package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */

public class PiecePlacementBuilder implements ChessRepresentationBuilder<PiecePlacement> {
	
	private final PiecePlacement piecePlacement;
	
	public PiecePlacementBuilder() {
		this(new ChessFactory());
	}
	
	public PiecePlacementBuilder(ChessFactory chessFactory) {
		this.piecePlacement = chessFactory.createPiecePlacement();
	}	
	
	@Override
	public PiecePlacement getResult() {
		return piecePlacement;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withTurn(Color turn) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<PiecePlacement> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<PiecePlacement> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<PiecePlacement> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	public ChessRepresentationBuilder<PiecePlacement> withPiece(Square square, Piece piece) {
		piecePlacement.setPieza(square, piece);
		return this;
	}


}
