package net.chesstango.board.moves.factories;

import net.chesstango.board.PiecePositioned;
import net.chesstango.board.iterators.Cardinal;
import net.chesstango.board.moves.Move;

/**
 * @author Mauricio Coria
 *
 */
public interface RookMoveFactory {
    Move createSimpleRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);

    Move createCaptureRookMove(PiecePositioned origen, PiecePositioned destino, Cardinal cardinal);
}
