package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.imp.MoveCommand;
import net.chesstango.board.moves.imp.MoveImp;

/**
 * @author Mauricio Coria
 *
 */
public interface QueenMoveFactory {
    MoveCommand createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    MoveCommand createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
