package chess.builder;

import chess.Color;
import chess.Game;
import chess.Piece;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public class ChessBuilderGame implements ChessBuilder {
	
	private ChessBuilderBoard builder = new ChessBuilderBoard();
	
	private Game game = null;
	
	public ChessBuilderGame() {
		this.builder = new ChessBuilderBoard();
	}
	
	public ChessBuilderGame(ChessFactory chessFactory) {
		this.builder = new ChessBuilderBoard(chessFactory);
	}	
	
	public Game getGame() {
		if (game == null) {
			game = new Game(builder.getBoard());
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
	public void withCastlingWhiteQueenPermitido(boolean enroqueBlancoQueenPermitido) {
		builder.withCastlingWhiteQueenPermitido(enroqueBlancoQueenPermitido);
	}

	@Override
	public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		builder.withCastlingWhiteKingPermitido(enroqueBlancoKingPermitido);
	}

	@Override
	public void withCastlingBlackQueenPermitido(boolean enroqueNegroQueenPermitido) {
		builder.withCastlingBlackQueenPermitido(enroqueNegroQueenPermitido);
	}

	@Override
	public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		builder.withCastlingBlackKingPermitido(enroqueNegroKingPermitido);
	}

	@Override
	public void withPieza(Square square, Piece piece) {
		builder.withPieza(square, piece);
	}

}
