package chess.builder;

import chess.BoardState;
import chess.Color;
import chess.Piece;
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
	public void withCastlingWhiteQueenPermitido(boolean enroqueBlancoQueenPermitido) {
		this.getState().setCastlingWhiteQueenPermitido(enroqueBlancoQueenPermitido);
	}

	@Override
	public void withCastlingWhiteKingPermitido(boolean enroqueBlancoKingPermitido) {
		this.getState().setCastlingWhiteKingPermitido(enroqueBlancoKingPermitido);;
	}


	@Override
	public void withCastlingBlackQueenPermitido(boolean enroqueNegroQueenPermitido) {
		this.getState().setCastlingBlackQueenPermitido(enroqueNegroQueenPermitido);
	}


	@Override
	public void withCastlingBlackKingPermitido(boolean enroqueNegroKingPermitido) {
		this.getState().setCastlingBlackKingPermitido(enroqueNegroKingPermitido);;
	}

	public void withPieza(Square square, Piece piece) {
		this.getPosicionPiezaBoard().setPieza(square, piece);
	}
	

}
