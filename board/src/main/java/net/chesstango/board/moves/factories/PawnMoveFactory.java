package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public interface PawnMoveFactory {

    PseudoMove createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to);

    PseudoMove createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare);

    PseudoMove createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    PseudoMove createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal);

    PseudoMove createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece);

    PseudoMove createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal);
}
