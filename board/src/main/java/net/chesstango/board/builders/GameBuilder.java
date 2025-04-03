package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.position.SquareBoard;
import net.chesstango.board.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class GameBuilder implements ChessPositionBuilder<Game> {
	
	private final SquareBoard squareBoard;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	
	private Game game = null;

	public GameBuilder() {
		this(new ChessInjector());
	}

	public GameBuilder(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}
	
	public GameBuilder(ChessInjector chessInjector) {
		this.squareBoard = chessInjector.getPiecePlacement();
		this.positionState =  chessInjector.getPositionState();
		this.chessInjector = chessInjector;
	}
	
	@Override
	public Game getChessRepresentation() {
		if (game == null) {
			game = chessInjector.getGame();
		}
		return game;
	}

	@Override
	public ChessPositionBuilder<Game> withTurn(Color turn) {
		positionState.setCurrentTurn(turn);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withEnPassantSquare(Square enPassantSquare) {
		positionState.setEnPassantSquare(enPassantSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		positionState.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		positionState.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		positionState.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		positionState.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
		return this;
	}

	public ChessPositionBuilder<Game> withPiece(Square square, Piece piece) {
		squareBoard.setPiece(square, piece);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withHalfMoveClock(int halfMoveClock) {
		positionState.setHalfMoveClock(halfMoveClock);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withFullMoveClock(int fullMoveClock) {
		positionState.setFullMoveClock(fullMoveClock);
		return this;
	}

}
