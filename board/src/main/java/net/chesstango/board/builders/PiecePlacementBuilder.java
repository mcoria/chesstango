package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.position.Board;


/**
 * @author Mauricio Coria
 *
 */

public class PiecePlacementBuilder implements ChessRepresentationBuilder<Board> {
	
	private final Board board;
	
	public PiecePlacementBuilder() {
		this(new ChessFactory());
	}
	
	public PiecePlacementBuilder(ChessFactory chessFactory) {
		this.board = chessFactory.createPiecePlacement();
	}	
	
	@Override
	public Board getChessRepresentation() {
		return board;
	}

	@Override
	public ChessRepresentationBuilder<Board> withTurn(Color turn) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Board> withEnPassantSquare(Square enPassantSquare) {
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Board> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Board> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Board> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Board> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Board> withHalfMoveClock(int halfMoveClock) {
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Board> withFullMoveClock(int fullMoveClock) {
		return this;
	}

	public ChessRepresentationBuilder<Board> withPiece(Square square, Piece piece) {
		board.setPiece(square, piece);
		return this;
	}


}
