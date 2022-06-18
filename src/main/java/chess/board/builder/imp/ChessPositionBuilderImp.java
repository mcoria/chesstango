package chess.board.builder.imp;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;
import chess.board.builder.ChessPositionBuilder;
import chess.board.factory.ChessFactory;
import chess.board.factory.ChessInjector;
import chess.board.position.ChessPosition;
import chess.board.position.PiecePlacement;
import chess.board.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class ChessPositionBuilderImp implements ChessPositionBuilder<ChessPosition> {
	
	private final PiecePlacement piecePlacement;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	private ChessPosition chessPosition = null;

	
	public ChessPositionBuilderImp(ChessInjector chessInjector) {
		this.piecePlacement = chessInjector.getPiecePlacement();
		this.positionState =  chessInjector.getPositionState();
		this.chessInjector = chessInjector;
	}
	
	public ChessPositionBuilderImp() {
		this(new ChessInjector());
	}	
	
	public ChessPositionBuilderImp(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}
	
	@Override
	public ChessPosition getResult() {
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
		return null;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withFullMoveClock(int fullMoveClock) {
		return null;
	}

	public ChessPositionBuilder<ChessPosition> withPiece(Square square, Piece piece) {
		piecePlacement.setPieza(square, piece);
		return this;
	}


}
