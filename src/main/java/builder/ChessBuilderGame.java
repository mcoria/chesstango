package builder;

import chess.Color;
import chess.Game;
import chess.Pieza;
import chess.Square;

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
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		builder.withEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		builder.withEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);
	}

	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		builder.withEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
	}

	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		builder.withEnroqueNegroReyPermitido(enroqueNegroReyPermitido);
	}

	@Override
	public void withPieza(Square square, Pieza pieza) {
		builder.withPieza(square, pieza);
	}

}
