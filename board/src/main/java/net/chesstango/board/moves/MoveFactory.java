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

	Move createSimplePawnMove(PiecePositioned origen, PiecePositioned destino);
	Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare);
	Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
	Move createCaptureEnPassant(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal, PiecePositioned capture);
	MovePromotion createSimplePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece);
	MovePromotion createCapturePawnPromotion(PiecePositioned origen, PiecePositioned destino, Piece piece);

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

}