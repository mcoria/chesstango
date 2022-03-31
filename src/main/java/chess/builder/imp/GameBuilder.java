package chess.builder.imp;

import chess.Color;
import chess.Game;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;
import chess.position.PiecePlacement;
import chess.position.imp.PositionState;

/**
 * @author Mauricio Coria
 *
 */
////TODO: El modelo de objetos es complejo, es necesario DI para inyectar dependencias
public class GameBuilder implements ChessPositionBuilder<Game> {
	
	private final PiecePlacement piecePlacement;
	private final PositionState positionState;
	private final ChessInjector chessInjector;
	
	private Game game = null;

	
	public GameBuilder(ChessInjector chessInjector) {
		this.piecePlacement = chessInjector.getPiecePlacement();
		this.positionState =  chessInjector.getPositionState();
		this.chessInjector = chessInjector;
	}
	
	public GameBuilder() {
		this(new ChessInjector());
	}
	
	public GameBuilder(ChessFactory chessFactory) {
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
		positionState.setTurnoActual(turno);
		return this;
	}


	@Override
	public ChessPositionBuilder<Game> withEnPassantSquare(Square pawnPasanteSquare) {
		positionState.setEnPassantSquare(pawnPasanteSquare);
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

	public ChessPositionBuilder<Game> withPieza(Square square, Piece piece) {
		piecePlacement.setPieza(square, piece);
		return this;
	}
	
}
