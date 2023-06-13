package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilder implements ChessRepresentationBuilder<ChessPosition> {
	
	private final SquareBoard squareBoard;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	private ChessPosition chessPosition = null;

	
	public ChessPositionBuilder(ChessInjector chessInjector) {
		this.squareBoard = chessInjector.getPiecePlacement();
		this.positionState =  chessInjector.getPositionState();
		this.chessInjector = chessInjector;
	}
	
	public ChessPositionBuilder() {
		this(new ChessInjector());
	}	
	
	public ChessPositionBuilder(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
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
	public ChessRepresentationBuilder<ChessPosition> withTurn(Color turn) {
		positionState.setCurrentTurn(turn);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<ChessPosition> withEnPassantSquare(Square enPassantSquare) {
		positionState.setEnPassantSquare(enPassantSquare);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<ChessPosition> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		positionState.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<ChessPosition> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		positionState.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<ChessPosition> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		positionState.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<ChessPosition> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		positionState.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<ChessPosition> withHalfMoveClock(int halfMoveClock) {
		positionState.setHalfMoveClock(halfMoveClock);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<ChessPosition> withFullMoveClock(int fullMoveClock) {
		positionState.setFullMoveClock(fullMoveClock);
		return this;
	}

	public ChessRepresentationBuilder<ChessPosition> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}


}
