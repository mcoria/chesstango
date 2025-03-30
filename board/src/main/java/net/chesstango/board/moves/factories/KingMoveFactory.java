package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.MoveCommand;

/**
 * @author Mauricio Coria
 *
 */
public interface KingMoveFactory {
    MoveCommand createSimpleKingMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createCaptureKingMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createCastlingQueenMove();

    MoveCommand createCastlingKingMove();
}
