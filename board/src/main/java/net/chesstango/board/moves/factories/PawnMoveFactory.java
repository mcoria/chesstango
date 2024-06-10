package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.MoveImp;
import net.chesstango.board.moves.imp.MovePromotionImp;

/**
 * @author Mauricio Coria
 *
 */
public interface PawnMoveFactory {

    MoveImp createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to);

    MoveImp createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare);

    MoveImp createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    MoveImp createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal);

    MovePromotionImp createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece);

    MovePromotionImp createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal);
}
