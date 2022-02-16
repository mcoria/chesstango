package chess.builder.imp;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;
import chess.factory.ChessFactory;
import chess.factory.ChessInjector;
import chess.position.PiecePlacement;


/**
 * @author Mauricio Coria
 *
 */

public class PiecePlacementBuilder implements ChessPositionBuilder<PiecePlacement> {
	
	private PiecePlacement piecePlacement = null;	
	
	private final ChessInjector chessInjector;
	
	public PiecePlacementBuilder() {
		this.chessInjector = new ChessInjector();
	}	
	
	public PiecePlacementBuilder(ChessInjector chessInjector) {
		this.chessInjector = chessInjector;
	}
	
	public PiecePlacementBuilder(ChessFactory chessFactory) {
		this(new ChessInjector(chessFactory));
	}	
	
	@Override
	public PiecePlacement getResult() {
		if (piecePlacement == null) {
			
			piecePlacement = chessInjector.getPiecePlacement();
		}
		return piecePlacement;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withTurno(Color turno) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withPawnPasanteSquare(Square peonPasanteSquare) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		return this;
	}

	public ChessPositionBuilder<PiecePlacement> withPieza(Square square, Piece piece) {
		chessInjector.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	

}
