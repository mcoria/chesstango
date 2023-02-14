package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.position.ChessPosition;
import net.chesstango.board.position.PiecePlacement;
import net.chesstango.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilder implements ChessRepresentationBuilder<ChessPosition> {
	
	private final PiecePlacement piecePlacement;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	private ChessPosition chessPosition = null;

	
	public ChessPositionBuilder(ChessInjector chessInjector) {
		this.piecePlacement = chessInjector.getPiecePlacement();
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
		return null;
	}

	@Override
	public ChessRepresentationBuilder<ChessPosition> withFullMoveClock(int fullMoveClock) {
		return null;
	}

	public ChessRepresentationBuilder<ChessPosition> withPiece(Square square, Piece piece) {
		piecePlacement.setPieza(square, piece);
		return this;
	}


}
