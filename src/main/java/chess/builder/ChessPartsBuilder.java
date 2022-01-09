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
public class ChessPartsBuilder implements ChessPositionBuilder {	
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private ChessFactory chessFactory = null;
	
	public ChessPartsBuilder(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}

	public PiecePlacement getPiecePlacement() {
		if(piecePlacement == null){
			piecePlacement = chessFactory.createPosicionPiezaBoard();
		}
		return piecePlacement;
	}

	public PositionState getPositionState() {
		if (positionState == null) {
			positionState = chessFactory.createBoardState();
		}
		return positionState;
	}


	@Override
	public void withTurno(Color turno) {
		this.getPositionState().setTurnoActual(turno);
	}


	@Override
	public void withPawnPasanteSquare(Square peonPasanteSquare) {
		this.getPositionState().setPawnPasanteSquare(peonPasanteSquare);
	}


	@Override
	public void withCastlingWhiteQueenAllowed(boolean enroqueBlancoQueenAllowed) {
		this.getPositionState().setCastlingWhiteQueenAllowed(enroqueBlancoQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueBlancoKingAllowed) {
		this.getPositionState().setCastlingWhiteKingAllowed(enroqueBlancoKingAllowed);;
	}


	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueNegroQueenAllowed) {
		this.getPositionState().setCastlingBlackQueenAllowed(enroqueNegroQueenAllowed);
	}


	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueNegroKingAllowed) {
		this.getPositionState().setCastlingBlackKingAllowed(enroqueNegroKingAllowed);;
	}

	public void withPieza(Square square, Piece piece) {
		this.getPiecePlacement().setPieza(square, piece);
	}
	

}
