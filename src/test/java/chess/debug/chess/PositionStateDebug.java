package chess.debug.chess;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.position.PiecePlacement;
import chess.position.imp.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class PositionStateDebug extends PositionState{

	public void validar() {
		if(this.getTurnoActual() == null){
			throw new RuntimeException("Y el turno?");
		}
		
		if(this.getEnPassantSquare() != null){
			if(Color.WHITE.equals(this.getTurnoActual()) && this.getEnPassantSquare().getRank() != 5){
				throw new RuntimeException("EnPassantSquare mal setteado: " + this.getEnPassantSquare());
			}
			if(Color.BLACK.equals(this.getTurnoActual()) && this.getEnPassantSquare().getRank() != 2){
				throw new RuntimeException("EnPassantSquare mal setteado: " + this.getEnPassantSquare());				
			}			
		}
		
	}
	
	public void validar(PiecePlacement board) {
		validar();

		if (this.isCastlingWhiteQueenAllowed()) {
			if (board.getPieza(Square.a1) == null) {
				throw new RuntimeException(
						"isCastlingWhiteQueenAllowed mal setteado");
			} else if (!Piece.ROOK_WHITE.equals(board.getPieza(Square.a1))) {
				throw new RuntimeException(
						"isCastlingWhiteQueenAllowed mal setteado");
			}
		}

		if (this.isCastlingWhiteKingAllowed()) {
			if (board.getPieza(Square.h1) == null) {
				throw new RuntimeException(
						"isCastlingWhiteQueenAllowed mal setteado");
			} else if (!Piece.ROOK_WHITE.equals(board.getPieza(Square.h1))) {
				throw new RuntimeException(
						"isCastlingWhiteQueenAllowed mal setteado");
			}
		}

		if (this.isCastlingBlackQueenAllowed()) {
			if (board.getPieza(Square.a8) == null) {
				throw new RuntimeException(
						"isCastlingBlackQueenAllowed mal setteado");
			} else if (!Piece.ROOK_BLACK.equals(board.getPieza(Square.a8))) {
				throw new RuntimeException(
						"isCastlingBlackQueenAllowed mal setteado");
			}
		}

		if (this.isCastlingBlackKingAllowed()) {
			if (board.getPieza(Square.h8) == null) {
				throw new RuntimeException(
						"isCastlingBlackKingAllowed mal setteado");
			} else if (!Piece.ROOK_BLACK.equals(board.getPieza(Square.h8))) {
				throw new RuntimeException(
						"isCastlingBlackKingAllowed mal setteado");
			}
		}
	}
}
