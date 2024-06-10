package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.imp.MoveCastlingImp;
import net.chesstango.board.moves.imp.MoveKingImp;

/**
 * @author Mauricio Coria
 *
 */
public interface KingMoveFactory {
    MoveKingImp createSimpleKingMove(PiecePositioned from, PiecePositioned to);

    MoveKingImp createCaptureKingMove(PiecePositioned from, PiecePositioned to);

    MoveCastlingImp createCastlingQueenMove();

    MoveCastlingImp createCastlingKingMove();
}
