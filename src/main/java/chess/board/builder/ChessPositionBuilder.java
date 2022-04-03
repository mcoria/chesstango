package chess.board.builder;

import chess.board.Color;
import chess.board.Piece;
import chess.board.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface ChessPositionBuilder<T> {

	ChessPositionBuilder<T> withTurno(Color turno);

	ChessPositionBuilder<T> withEnPassantSquare(Square pawnPasanteSquare);

	ChessPositionBuilder<T> withCastlingWhiteQueenAllowed(boolean castlingWhiteQueenAllowed);

	ChessPositionBuilder<T> withCastlingWhiteKingAllowed(boolean castlingWhiteKingAllowed);

	ChessPositionBuilder<T> withCastlingBlackQueenAllowed(boolean castlingBlackQueenAllowed);

	ChessPositionBuilder<T> withCastlingBlackKingAllowed(boolean castlingBlackKingAllowed);
	
	ChessPositionBuilder<T> withPieza(Square square, Piece piece);
	
	
	T getResult();

}