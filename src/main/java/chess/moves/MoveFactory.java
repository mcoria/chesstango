/**
 * 
 */
package chess.moves;

import chess.Piece;
import chess.PiecePositioned;
import chess.Square;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveFactory {
	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);
	
	Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino);
	
	Move createSaltoDoblePawnMove(PiecePositioned origen, PiecePositioned destino, Square saltoSimpleCasillero);
	Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, PiecePositioned capturaEnPassant);

	Move createSimplePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);
	Move createCapturePawnPromocion(PiecePositioned origen, PiecePositioned destino, Piece piece);

	MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino);
	MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino);
	
	MoveCastling createCastlingQueenMove();
	MoveCastling createCastlingKingMove();

}