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
	public ChessPositionBuilder<PiecePlacement> withEnPassantSquare(Square pawnPasanteSquare) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		return this;
	}

	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		return this;
	}


	@Override
	public ChessPositionBuilder<PiecePlacement> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		return this;
	}

	public ChessPositionBuilder<PiecePlacement> withPieza(Square square, Piece piece) {
		chessInjector.getPiecePlacement().setPieza(square, piece);
		return this;
	}
	

}
