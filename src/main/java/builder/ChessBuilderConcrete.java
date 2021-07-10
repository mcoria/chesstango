package builder;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;
import layers.imp.ArrayPosicionPiezaBoard;


public class ChessBuilderConcrete implements ChessBuilder {

	private Color turno;

	private Square peonPasanteSquare;

	private boolean enroqueBlancoReinaPermitido;

	private boolean enroqueBlancoReyPermitido;

	private boolean enroqueNegroReinaPermitido;
	
	private boolean enroqueNegroReyPermitido;	
	
	private PosicionPiezaBoard posicionPiezaBoard;
	
	private BoardState boardState;

	public PosicionPiezaBoard getPosicionPiezaBoard() {
		if(posicionPiezaBoard == null){
			posicionPiezaBoard = new ArrayPosicionPiezaBoard();
		}
		return posicionPiezaBoard;
	}

	public BoardState getState() {
		if (boardState == null) {
			boardState = new BoardState();
			boardState.setTurnoActual(this.turno == null ? Color.BLANCO : this.turno);
			boardState.setPeonPasanteSquare(peonPasanteSquare);
			boardState.setEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
			boardState.setEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);
			boardState.setEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
			boardState.setEnroqueNegroReyPermitido(enroqueNegroReyPermitido);
		}
		return boardState;
	}


	@Override
	public void withTurno(Color turno) {
		this.turno = turno;
	}


	@Override
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		this.peonPasanteSquare = peonPasanteSquare;
	}


	@Override
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.enroqueBlancoReinaPermitido = enroqueBlancoReinaPermitido;
	}

	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.enroqueBlancoReyPermitido = enroqueBlancoReyPermitido;
	}


	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.enroqueNegroReinaPermitido = enroqueNegroReinaPermitido;
	}


	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.enroqueNegroReyPermitido = enroqueNegroReyPermitido;
	}

	public void withPieza(Square square, Pieza pieza) {
		getPosicionPiezaBoard().setPieza(square, pieza);
	}
	

}
