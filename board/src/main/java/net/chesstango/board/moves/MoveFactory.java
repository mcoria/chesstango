package net.chesstango.board.moves;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;

/**
 * @author Mauricio Coria
 *
 */
public interface MoveFactory {

	Move createSimpleOneSquarePawnMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare);
	Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
	Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino, PiecePositioned enPassantPawn, Cardinal cardinal);

	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino);
	Move createCaptureMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
	Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

	MoveKing createSimpleKingMove(PiecePositioned origen, PiecePositioned destino);
	MoveKing createCaptureKingMove(PiecePositioned origen, PiecePositioned destino);
	
	MoveCastling createCastlingQueenMove();
	MoveCastling createCastlingKingMove();

	MovePromotion createSimplePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece);
	MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece, Cardinal cardinal);
}