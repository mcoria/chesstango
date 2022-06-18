/**
 * 
 */
package chess.board.moves;

import chess.board.Piece;
import chess.board.PiecePositioned;
import chess.board.Square;
import chess.board.iterators.Cardinal;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveFactory {
	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal norte);
	Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino);

	
	Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero, Cardinal norte);

	Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, PiecePositioned capturaEnPassant);

	Move createSimplePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);
	Move createCapturePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);

	MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino);
	MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino);
	
	MoveCastling createCastlingQueenMove();
	MoveCastling createCastlingKingMove();

}