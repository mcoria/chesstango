package chess.builder;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.position.PiecePlacement;
import chess.position.PositionState;


/**
 * @author Mauricio Coria
 *
 */
public class ChessBuilderParts implements ChessBuilder {	
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private ChessFactory chessFactory = null;
	
	public ChessBuilderParts(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}

	public PiecePlacement getPosicionPiezaBoard() {
		if(piecePlacement == null){
			piecePlacement = chessFactory.createPosicionPiezaBoard();
		}
		return piecePlacement;
	}

	public PositionState getState() {
		if (positionState == null) {
			positionState = chessFactory.createBoardState();
		}
		return positionState;
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
	public void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		this.getState().setCastlingWhiteQueenAllowed(enroqueBlancoQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		this.getState().setCastlingWhiteKingAllowed(enroqueBlancoKingAllowed);;
	}


	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		this.getState().setCastlingBlackQueenAllowed(enroqueNegroQueenAllowed);
	}


	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		this.getState().setCastlingBlackKingAllowed(enroqueNegroKingAllowed);;
	}

	public void withPieza(Square square, Piece piece) {
		this.getPosicionPiezaBoard().setPieza(square, piece);
	}
	

}
