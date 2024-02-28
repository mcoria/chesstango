package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface QueenMoveFactory {
    Move createSimpleQueenMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

    Move createCaptureQueenMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
}
