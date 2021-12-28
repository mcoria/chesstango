package debug.chess;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;


/**
 * @author Mauricio Coria
 *
 */
public class BoardStateDebug extends BoardState{

	public void validar() {
		if(this.getTurnoActual() == null){
			throw new RuntimeException("Y el turno?");
		}
		
		if(this.getPeonPasanteSquare() != null){
			if(Color.BLANCO.equals(this.getTurnoActual()) && this.getPeonPasanteSquare().getRank() != 5){
				throw new RuntimeException("PeonPasanteSquare mal setteado: " + this.getPeonPasanteSquare());
			}
			if(Color.NEGRO.equals(this.getTurnoActual()) && this.getPeonPasanteSquare().getRank() != 2){
				throw new RuntimeException("PeonPasanteSquare mal setteado: " + this.getPeonPasanteSquare());				
			}			
		}
		
	}
	
	public void validar(PosicionPiezaBoard board) {
		validar();

		if (this.isEnroqueBlancoReinaPermitido()) {
			if (board.getPieza(Square.a1) == null) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			} else if (!Pieza.TORRE_BLANCO.equals(board.getPieza(Square.a1))) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			}
		}

		if (this.isEnroqueBlancoReyPermitido()) {
			if (board.getPieza(Square.h1) == null) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			} else if (!Pieza.TORRE_BLANCO.equals(board.getPieza(Square.h1))) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			}
		}

		if (this.isEnroqueNegroReinaPermitido()) {
			if (board.getPieza(Square.a8) == null) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			} else if (!Pieza.TORRE_NEGRO.equals(board.getPieza(Square.a8))) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			}
		}

		if (this.isEnroqueNegroReyPermitido()) {
			if (board.getPieza(Square.h8) == null) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			} else if (!Pieza.TORRE_NEGRO.equals(board.getPieza(Square.h8))) {
				throw new RuntimeException(
						"isEnroqueBlancoReinaPermitido mal setteado: " + this.getPeonPasanteSquare());
			}
		}
	}
}
