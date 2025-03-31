package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.PseudoMove;

/**
 * @author Mauricio Coria
 *
 */
public interface QueenMoveFactory {
    PseudoMove createSimpleQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);

    PseudoMove createCaptureQueenMove(PiecePositioned from, PiecePositioned to, Cardinal cardinal);
}
