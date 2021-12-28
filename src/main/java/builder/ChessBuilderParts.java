package builder;

import chess.BoardState;
import chess.Color;
import chess.Pieza;
import chess.Square;
import layers.PosicionPiezaBoard;


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
	public void withEnroqueBlancoReinaPermitido(boolean enroqueBlancoReinaPermitido) {
		this.getState().setEnroqueBlancoReinaPermitido(enroqueBlancoReinaPermitido);
	}

	@Override
	public void withEnroqueBlancoReyPermitido(boolean enroqueBlancoReyPermitido) {
		this.getState().setEnroqueBlancoReyPermitido(enroqueBlancoReyPermitido);;
	}


	@Override
	public void withEnroqueNegroReinaPermitido(boolean enroqueNegroReinaPermitido) {
		this.getState().setEnroqueNegroReinaPermitido(enroqueNegroReinaPermitido);
	}


	@Override
	public void withEnroqueNegroReyPermitido(boolean enroqueNegroReyPermitido) {
		this.getState().setEnroqueNegroReyPermitido(enroqueNegroReyPermitido);;
	}

	public void withPieza(Square square, Pieza pieza) {
		this.getPosicionPiezaBoard().setPieza(square, pieza);
	}
	

}
