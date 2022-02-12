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
	
	public ChessPositionBuilderGame() {
		this.chessInjector = new ChessInjector();
	}	
	
	public ChessPositionBuilderGame(ChessInjector chessInjector) {
		this.chessInjector = chessInjector;
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
	public ChessPositionBuilder<Game> withPawnPasanteSquare(Square peonPasanteSquare) {
		chessInjector.getPositionState().setPawnPasanteSquare(peonPasanteSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		chessInjector.getPositionState().setCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<Game> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		chessInjector.getPositionState().setCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		chessInjector.getPositionState().setCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		chessInjector.getPositionState().setCastlingBlackKingAllowed(enroqueBlackKingAllowed);
		return this;
	}

	public ChessPositionBuilder<Game> withPieza(Square square, Piece piece) {
		chessInjector.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	
}
