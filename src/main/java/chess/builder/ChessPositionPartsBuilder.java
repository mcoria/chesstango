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
public class ChessPositionPartsBuilder implements ChessPositionBuilder {	
	
	private PiecePlacement piecePlacement;
	
	private PositionState positionState;
	
	private ChessFactory chessFactory = null;
	
	public ChessPositionPartsBuilder(ChessFactory chessFactory) {
		this.chessFactory = chessFactory;
	}

	public PiecePlacement getPiecePlacement() {
		if(piecePlacement == null){
			piecePlacement = chessFactory.createPiecePlacement();
		}
		return piecePlacement;
	}

	public PositionState getPositionState() {
		if (positionState == null) {
			positionState = chessFactory.createPositionState();
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
	public void withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		this.getPositionState().setCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
	}

	@Override
	public void withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		this.getPositionState().setCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);;
	}


	@Override
	public void withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		this.getPositionState().setCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
	}


	@Override
	public void withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		this.getPositionState().setCastlingBlackKingAllowed(enroqueBlackKingAllowed);;
	}

	public void withPieza(Square square, Piece piece) {
		this.getPiecePlacement().setPieza(square, piece);
	}
	

}
