package chess.builder.imp;

import chess.Color;
import chess.Game;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;

/**
 * @author Mauricio Coria
 *
 */
////TODO: El modelo de objetos es complejo, es necesario DI para inyectar dependencias
public class ChessPositionBuilderGame implements ChessPositionBuilder<Game> {
	
	private Game game = null;
	
	private final ChessInjector chessInjector;
	
	public ChessPositionBuilderGame(ChessInjector chessInjector) {
		this.chessInjector = chessInjector;
	}
	
	public ChessPositionBuilderGame() {
		this(new ChessInjector());
	}
	
	public ChessPositionBuilderGame(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}	
	
	@Override
	public Game getResult() {
		if (game == null) {
			game = chessInjector.getGame();
			game.init();
		}
		return game;
	}

	@Override
	public ChessPositionBuilder<Game> withTurno(Color turno) {
		chessInjector.getPositionState().setTurnoActual(turno);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withEnPassantSquare(Square pawnPasanteSquare) {
		chessInjector.getPositionState().setEnPassantSquare(pawnPasanteSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		chessInjector.getPositionState().setCastlingWhiteQueenAllowed(castlingWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		chessInjector.getPositionState().setCastlingWhiteKingAllowed(castlingWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		chessInjector.getPositionState().setCastlingBlackQueenAllowed(castlingBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		chessInjector.getPositionState().setCastlingBlackKingAllowed(castlingBlackKingAllowed);
		return this;
	}

	public ChessPositionBuilder<Game> withPieza(Square square, Piece piece) {
		chessInjector.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	
}
