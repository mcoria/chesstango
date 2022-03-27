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
	protected boolean enroqueBlackKingAllowed;
	protected boolean enroqueBlackQueenAllowed;
	protected boolean enroqueWhiteKingAllowed;
	protected boolean enroqueWhiteQueenAllowed;
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
	public ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean enroqueBlackKingAllowed) {
		this.enroqueBlackKingAllowed = enroqueBlackKingAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean enroqueBlackQueenAllowed) {
		this.enroqueBlackQueenAllowed = enroqueBlackQueenAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean enroqueWhiteKingAllowed) {
		this.enroqueWhiteKingAllowed = enroqueWhiteKingAllowed;
		return this;
	}

	@Override
	public ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean enroqueWhiteQueenAllowed) {
		this.enroqueWhiteQueenAllowed = enroqueWhiteQueenAllowed;
		return this;
	}

}
