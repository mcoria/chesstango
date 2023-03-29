package net.chesstango.board.builders;

import net.chesstango.board.Color;
import net.chesstango.board.Game;
import net.chesstango.board.Piece;
import net.chesstango.board.Square;
import net.chesstango.board.factory.ChessFactory;
import net.chesstango.board.factory.ChessInjector;
import net.chesstango.board.position.Board;
import net.chesstango.board.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
public class GameBuilder implements ChessRepresentationBuilder<Game> {
	
	private final Board board;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	
	private Game game = null;

	public GameBuilder(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}

	public GameBuilder() {
		this(new ChessInjector());
	}
	
	public GameBuilder(ChessInjector chessInjector) {
		this.board = chessInjector.getPiecePlacement();
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
	public ChessRepresentationBuilder<Game> withTurn(Color turn) {
		positionState.setCurrentTurn(turn);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Game> withEnPassantSquare(Square enPassantSquare) {
		positionState.setEnPassantSquare(enPassantSquare);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Game> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		positionState.setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Game> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		positionState.setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Game> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		positionState.setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessRepresentationBuilder<Game> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		positionState.setCastlingBlackKingAllowed(castlingBlackKingAllowed);
		return this;
	}

	public ChessRepresentationBuilder<Game> withPiece(Square square, Piece piece) {
		board.setPieza(square, piece);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Game> withHalfMoveClock(int halfMoveClock) {
		positionState.setHalfMoveClock(halfMoveClock);
		return this;
	}

	@Override
	public ChessRepresentationBuilder<Game> withFullMoveClock(int fullMoveClock) {
		positionState.setFullMoveClock(fullMoveClock);
		return this;
	}

}
