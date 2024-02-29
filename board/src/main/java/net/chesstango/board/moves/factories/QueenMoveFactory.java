package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface QueenMoveFactory {
    Move createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    Move createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
