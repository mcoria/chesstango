package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.ChessPositionWriter;
import net.chesstango.board.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class DefaultChessPositionBuilder implements ChessPositionBuilder<ChessPosition> {
	
	private final SquareBoard squareBoard;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	private ChessPosition chessPosition = null;


	public DefaultChessPositionBuilder() {
		this(new ChessInjector());
	}

	DefaultChessPositionBuilder(ChessInjector chessInjector) {
		this.squareBoard = chessInjector.getPiecePlacement();
		this.positionState =  chessInjector.getPositionState();
		this.chessInjector = chessInjector;
	}
	
	@Override
	public ChessPosition getChessRepresentation() {
		if (chessPosition == null) {
			
			chessPosition = chessInjector.getChessPosition();
			
			chessPosition.init();
		}
		return chessPosition;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withTurn(Color turn) {
		positionState.setCurrentTurn(turn);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withEnPassantSquare(Square enPassantSquare) {
		positionState.setEnPassantSquare(enPassantSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		positionState.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		positionState.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		positionState.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		positionState.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withHalfMoveClock(int halfMoveClock) {
		positionState.setHalfMoveClock(halfMoveClock);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withFullMoveClock(int fullMoveClock) {
		positionState.setFullMoveClock(fullMoveClock);
		return this;
	}

	public ChessPositionBuilder<ChessPosition> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}


}
