package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;
import net.chesstango.board.moves.MovePromotion;

/**
 * @author Mauricio Coria
 *
 */
public interface PawnMoveFactory {

    Move createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to);

    Move createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare);

    Move createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    Move createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal);

    MovePromotion createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece);

    MovePromotion createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal);
}
