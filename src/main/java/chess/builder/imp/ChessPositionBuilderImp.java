package chess.builder.imp;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;
import chess.position.ChessPosition;


/**
 * @author Mauricio Coria
 *
 */
//TODO: El modelo de objetos es complejo, es necesario DI para inyectar dependencias
public class ChessPositionBuilderImp implements ChessPositionBuilder<ChessPosition> {
	
	private ChessPosition chessPosition = null;	
	
	private final ChessInjector chessInjector;
	
	public ChessPositionBuilderImp() {
		this.chessInjector = new ChessInjector();
	}	
	
	public ChessPositionBuilderImp(ChessInjector chessInjector) {
		this.chessInjector = chessInjector;
	}
	
	public ChessPositionBuilderImp(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}	
	
	@Override
	public ChessPosition getResult() {
		if (chessPosition == null) {
			
			chessPosition = chessInjector.getChessPosition();
			
			chessPosition.init();
		}
		return chessPosition;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withTurno(Color turno) {
		chessInjector.getPositionState().setTurnoActual(turno);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withPawnPasanteSquare(Square peonPasanteSquare) {
		chessInjector.getPositionState().setPawnPasanteSquare(peonPasanteSquare);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		chessInjector.getPositionState().setCastlingWhiteQueenAllowed(enroqueWhiteQueenAllowed);
		return this;
	}

	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		chessInjector.getPositionState().setCastlingWhiteKingAllowed(enroqueWhiteKingAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		chessInjector.getPositionState().setCastlingBlackQueenAllowed(enroqueBlackQueenAllowed);
		return this;
	}


	@Override
	public ChessPositionBuilder<ChessPosition> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		chessInjector.getPositionState().setCastlingBlackKingAllowed(enroqueBlackKingAllowed);
		return this;
	}

	public ChessPositionBuilder<ChessPosition> withPieza(Square square, Piece piece) {
		chessInjector.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	

}
