package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 */
public interface KnightMoveFactory {
    MoveCommand createSimpleKnightMove(PiecePositioned from, PiecePositioned to);

    MoveCommand createCaptureKnightMove(PiecePositioned from, PiecePositioned to);
}
