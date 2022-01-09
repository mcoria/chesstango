package chess.builder;

import chess.Color;
import chess.Game;
import chess.Piece;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class ChessGameBuilder implements ChessPositionBuilder {
	
	private ChessPositionBuilderImp builder = null;
	
	private Game game = null;
	
	public ChessGameBuilder() {
		this(new ChessFactory());
	}
	
	public ChessGameBuilder(ChessFactory chessFactory) {
		this.builder = new ChessPositionBuilderImp(chessFactory);
	}	
	
	public Game getGame() {
		if (game == null) {
			game = new Game(builder.getChessPosition());
		}
		return game;
	}
	
	@Override
	public void withTurno(Color turno) {
		builder.withTurno(turno);
	}

	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		builder.withPawnPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		builder.withCastlingWhiteQueenAllowed(enroqueBlancoQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		builder.withCastlingWhiteKingAllowed(enroqueBlancoKingAllowed);
	}

	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		builder.withCastlingBlackQueenAllowed(enroqueNegroQueenAllowed);
	}

	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		builder.withCastlingBlackKingAllowed(enroqueNegroKingAllowed);
	}

	@Override
	public void withPieza(Square square, Piece piece) {
		builder.withPieza(square, piece);
	}

}
