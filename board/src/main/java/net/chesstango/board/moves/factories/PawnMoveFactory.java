package net.chesstango.board.moves.factories;

import net.chesstango.board.Piece;
import net.chesstango.board.PiecePositioned;
import net.chesstango.board.Square;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.MoveCommand;

/**
 * @author Mauricio Coria
 *
 */
public interface PawnMoveFactory {

    MoveCommand createSimpleOneSquarePawnMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createSimpleTwoSquaresPawnMove(PiecePositioned from, PiecePositioned to, Square enPassantSquare);

    MoveCommand createCapturePawnMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    MoveCommand createCaptureEnPassantPawnMove(PiecePositioned from, PiecePositioned to, PiecePositioned enPassantPawn, Cardinal cardinal);

    MoveCommand createSimplePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece);

    MoveCommand createCapturePromotionPawnMove(PiecePositioned from, PiecePositioned to, Piece piece, Cardinal cardinal);
}
