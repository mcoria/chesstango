package chess.builder;

import chess.Color;
import chess.Game;
import chess.Pieza;
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
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		builder.withPeonPasanteSquare(peonPasanteSquare);
	}

	@Override
	public void withCastleWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		builder.withCastleWhiteReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withCastleWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		builder.withCastleWhiteKingPermitido(enroqueBlancoKingPermitido);
	}

	@Override
	public void withCastleBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		builder.withCastleBlackReinaPermitido(enroqueNegroReinaPermitido);
	}

	@Override
	public void withCastleBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		builder.withCastleBlackKingPermitido(enroqueNegroKingPermitido);
	}

	@Override
	public void withPieza(Square square, Pieza pieza) {
		builder.withPieza(square, pieza);
	}

}
