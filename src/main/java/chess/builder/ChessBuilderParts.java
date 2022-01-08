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
	public void withPeonPasanteSquare(Square peonPasanteSquare) {
		this.getState().setPeonPasanteSquare(peonPasanteSquare);
	}


	@Override
	public void withCastleWhiteReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.getState().setCastleWhiteReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withCastleWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		this.getState().setCastleWhiteKingPermitido(enroqueBlancoKingPermitido);;
	}


	@Override
	public void withCastleBlackReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.getState().setCastleBlackReinaPermitido(enroqueNegroReinaPermitido);
	}


	@Override
	public void withCastleBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		this.getState().setCastleBlackKingPermitido(enroqueNegroKingPermitido);;
	}

	public void withPieza(Square square, Pieza pieza) {
		this.getPosicionPiezaBoard().setPieza(square, pieza);
	}
	

}
