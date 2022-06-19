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

	Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare);
	Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
	Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, PiecePositioned capturaEnPassant);
	Move createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece);
	Move createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece);

	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino);

	MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino);
	MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino);
	
	MoveCastling createCastlingQueenMove();
	MoveCastling createCastlingKingMove();

}