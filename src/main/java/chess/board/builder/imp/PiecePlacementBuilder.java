package chess.board.builder.imp;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.factory.ChessFactory;
import chess.board.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */

public class PiecePlacementBuilder implements ChessPositionBuilder<PiecePlacement> {
	
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
	public ChessPositionBuilder<PiecePlacement> withTurn(Color turn) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	public ChessPositionBuilder<PiecePlacement> withPiece(Square square, Piece piece) {
		piecePlacement.setPieza(square, piece);
		return this;
	}


}
