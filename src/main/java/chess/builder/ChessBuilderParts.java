package chess.builder;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import chess.layers.PosicionPiezaBoard;


/**
 * @author Mauricio Coria
 *
 */
public class ChessBuilderParts implements ChessBuilder {	
	
	private PosicionPiezaBoard posicionPiezaBoard;
	
	private BoardState boardState;
	
	private ChessFactory chessFactory = null;
	
	public ChessBuilderParts(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}

	public PosicionPiezaBoard getPosicionPiezaBoard() {
		if(posicionPiezaBoard == null){
			posicionPiezaBoard = chessFactory.createPosicionPiezaBoard();
		}
		return posicionPiezaBoard;
	}

	public BoardState getState() {
		if (boardState == null) {
			boardState = chessFactory.createBoardState();
		}
		return boardState;
	}


	@Override
	public void withTurno(Color turno) {
		this.getState().setTurnoActual(turno);
	}


	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		this.getState().setPawnPasanteSquare(peonPasanteSquare);
	}


	@Override
	public void withCastlingWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.getState().setCastlingWhiteReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		this.getState().setCastlingWhiteKingPermitido(enroqueBlancoKingPermitido);;
	}


	@Override
	public void withCastlingBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.getState().setCastlingBlackReinaPermitido(enroqueNegroReinaPermitido);
	}


	@Override
	public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		this.getState().setCastlingBlackKingPermitido(enroqueNegroKingPermitido);;
	}

	public void withPieza(Square square, Pieza pieza) {
		this.getPosicionPiezaBoard().setPieza(square, pieza);
	}
	

}
