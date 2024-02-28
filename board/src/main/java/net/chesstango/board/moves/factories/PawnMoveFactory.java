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

    Move createSimpleOneSquarePawnMove(PiecePositioned origen, PiecePositioned destino);

    Move createSimpleTwoSquaresPawnMove(PiecePositioned origen, PiecePositioned destino, Square enPassantSquare);

    Move createCapturePawnMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

    Move createCaptureEnPassantPawnMove(PiecePositioned origen, PiecePositioned destino, PiecePositioned enPassantPawn, Cardinal cardinal);

    MovePromotion createSimplePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece);

    MovePromotion createCapturePromotionPawnMove(PiecePositioned origen, PiecePositioned destino, Piece piece, Cardinal cardinal);
}
