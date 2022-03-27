/**
 * 
 */
package chess.builder.imp;

import chess.Color;
import chess.Piece;
import chess.Square;
import chess.builder.ChessPositionBuilder;

/**
 * @author Mauricio Coria
 *
 */
public abstract class AbstractChessPositionBuilder<T> implements ChessPositionBuilder<T> {
	protected Color turno;
	protected Square pawnPasanteSquare;
	protected boolean castlingBlackKingAllowed;
	protected boolean castlingBlackQueenAllowed;
	protected boolean castlingWhiteKingAllowed;
	protected boolean castlingWhiteQueenAllowed;
	protected Piece[][] tablero = new Piece[8][8];

	@Override
	public ChessPositionBuilder<T> withPieza(Square square, Piece piece) {
		this.tablero[square.getRank()][square.getFile()] = piece;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withTurno(Color turno) {
		this.turno = turno;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withEnPassantSquare(Square pawnPasanteSquare) {
		this.pawnPasanteSquare = pawnPasanteSquare;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed) {
		this.castlingBlackKingAllowed = castlingBlackKingAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed) {
		this.castlingBlackQueenAllowed = castlingBlackQueenAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed) {
		this.castlingWhiteKingAllowed = castlingWhiteKingAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed) {
		this.castlingWhiteQueenAllowed = castlingWhiteQueenAllowed;
		return this;
	}

}
