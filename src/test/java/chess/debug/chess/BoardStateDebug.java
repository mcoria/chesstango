package chess.debug.chess;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.layers.PosicionPiezaBoard;


/**
 * @author Mauricio Coria
 *
 */
public class BoardStateDebug extends BoardState{

	public void validar() {
		if(this.getTurnoActual() == null){
			throw new RuntimeException("Y el turno?");
		}
		
		if(this.getPawnPasanteSquare() != null){
			if(Color.WHITE.equals(this.getTurnoActual()) && this.getPawnPasanteSquare().getRank() != 5){
				throw new RuntimeException("PawnPasanteSquare mal setteado: " + this.getPawnPasanteSquare());
			}
			if(Color.BLACK.equals(this.getTurnoActual()) && this.getPawnPasanteSquare().getRank() != 2){
				throw new RuntimeException("PawnPasanteSquare mal setteado: " + this.getPawnPasanteSquare());				
			}			
		}
		
	}
	
	public void validar(PosicionPiezaBoard board) {
		validar();

		if (this.isCastlingWhiteReinaPermitido()) {
			if (board.getPieza(Square.a1) == null) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			} else if (!Pieza.ROOK_WHITE.equals(board.getPieza(Square.a1))) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			}
		}

		if (this.isCastlingWhiteKingPermitido()) {
			if (board.getPieza(Square.h1) == null) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			} else if (!Pieza.ROOK_WHITE.equals(board.getPieza(Square.h1))) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			}
		}

		if (this.isCastlingBlackReinaPermitido()) {
			if (board.getPieza(Square.a8) == null) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			} else if (!Pieza.ROOK_BLACK.equals(board.getPieza(Square.a8))) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			}
		}

		if (this.isCastlingBlackKingPermitido()) {
			if (board.getPieza(Square.h8) == null) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			} else if (!Pieza.ROOK_BLACK.equals(board.getPieza(Square.h8))) {
				throw new RuntimeException(
						"isCastlingWhiteReinaPermitido mal setteado: " + this.getPawnPasanteSquare());
			}
		}
	}
}
