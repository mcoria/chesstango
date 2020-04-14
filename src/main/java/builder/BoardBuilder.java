package builder;

import chess.Game;
import chess.BoardState;
import chess.Color;
import chess.Board;
import chess.Pieza;
import chess.Square;

public abstract class BoardBuilder {

	private Pieza[][] tablero;
	
	private Color turno;
	
	private Square peonPasanteSquare;

	private boolean enroqueBlancoReinaPermitido;

	private boolean enroqueBlancoReyPermitido;

	private boolean enroqueNegroReinaPermitido;

	private boolean enroqueNegroReyPermitido;
	
	public Game buildBoard(){
		return new Game(buildDummyBoard());
	}
	
	public Board buildDummyBoard(){
		return new Board(getTablero(), getState());
	}
	
	protected Pieza[][] getTablero() {
		return this.tablero;
	}

	protected BoardState getState() {
		BoardState state = new BoardState();
		state.setTurnoActual(this.turno == null ? Color.BLANCO : this.turno);
		state.setPeonPasanteSquare(peonPasanteSquare);
		state.setEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
		state.setEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);
		state.setEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
		state.setEnroqueNegroReyPermitido(enroqueNegroReyPermitido);
		return state;
	}

	public BoardBuilder withTablero(Pieza[][] tablero) {
		this.tablero = tablero;
		return this;
	}

	public BoardBuilder withTurno(Color turno) {
		this.turno = turno;
		return this;
	}

	public BoardBuilder withPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
		return this;
	}

	public BoardBuilder withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
		return this;
	}

	public BoardBuilder withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
		return this;
	}

	public BoardBuilder withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
		return this;
	}

	public BoardBuilder withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
		return this;
	}


}
